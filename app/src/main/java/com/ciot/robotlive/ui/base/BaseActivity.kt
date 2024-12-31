package com.ciot.robotlive.ui.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ciot.robotlive.utils.ContextUtil
import com.ciot.robotlive.utils.MyLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by p'c on 2024/12/30.
 * Description:
 * Encoding: utf-8
 */
abstract class BaseActivity<P : BasePresenter<*>> : AppCompatActivity() {
    companion object {
        const val TAG = "BaseActivity"
    }

    private var mCompositeDisposable: CompositeDisposable? = null
    protected abstract fun createPresent(): P?
    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBottomUIMenu()
        mPresenter = createPresent()
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected open fun hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        try {
            if (Build.VERSION.SDK_INT in 12..18) { // lower api
                val v = this.window.decorView
                v.systemUiVisibility = View.GONE
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                val decorView = window.decorView
                val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
                decorView.systemUiVisibility = uiOptions
            }
        } catch (e: Exception) {
            MyLog.d(TAG, "hideBottomUIMenu Exception$e")
        }
    }

    /**
     * 检查权限
     *
     * @param neededPermissions 需要请求的权限
     * @return 有否有权限
     */
    protected open fun checkPermissions(neededPermissions: Array<String>): Boolean {
        if (neededPermissions.isEmpty()) {
            return true
        }
        var allGranted = true
        for (neededPermission in neededPermissions) {
            allGranted = allGranted and (ContextCompat.checkSelfPermission(ContextUtil.getContext(),
                neededPermission
            ) == PackageManager.PERMISSION_GRANTED)
        }
        return allGranted
    }

    fun addSubscription(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let { mCompositeDisposable!!.add(disposable) }
    }

    private fun onUnsubscribe() {
        mCompositeDisposable?.clear()
    }

    override fun onPause() {
        super.onPause()
        MyLog.d(TAG, "SystemActivity onPause")
        if (mPresenter != null) {
            mPresenter!!.detechView()
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomUIMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter!!.detechView()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            val v = currentFocus

            if (hideKeyboard(v, ev)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)

            val left = location[0]
            val top = location[1]
            val bottom = top + view.getHeight()
            val right = left + view.getWidth()
            val isInEt = event.x > left && event.x < right && event.y > top && event.y < bottom
            return !isInEt
        }
        return false
    }
}