package com.ciot.robotlive

import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.blankj.utilcode.util.ThreadUtils
import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.constant.ConstantLogic
import com.ciot.robotlive.constant.NetConstant
import com.ciot.robotlive.databinding.ActivityMainBinding
import com.ciot.robotlive.network.RetrofitManager
import com.ciot.robotlive.ui.base.BasePresenter
import com.ciot.robotlive.utils.MyLog
import com.ciot.robotlive.utils.SPUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedList
import java.util.Locale

/**
 * Created by p'c on 2024/12/30.
 * Description:
 * Encoding: utf-8
 */
class MainPresenter(private var view: MainActivity) : BasePresenter<MainView>() {
    companion object {
        private const val TAG = "MainPresenter"
    }
    var mBundle: Bundle? = null
    private var mContext: Context? = null
    var data: LinkedList<DealResult>? = null
        private set
    private var spUtils: SPUtils? = null

    init {
        initListener()
    }

    private fun initListener() {

    }

    fun initSetting() {
        setDefaultServer()
        getCurTime(view.binding)
        if (spUtils?.getInstance()?.getBoolean(ConstantLogic.SP_IS_SIGNED_IN, true) == false) {
            view.showSign()
        }
    }

    // 自动对时
    private lateinit var curTimeHandler: Handler
    private lateinit var updateTimeRunnable: Runnable
    private fun getCurTime(binding: ActivityMainBinding) {
        curTimeHandler = Handler()
        updateTimeRunnable = object : Runnable {
            override fun run() {
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                binding.headView.tvHeadTime.text = timeFormat.format(Date())
                curTimeHandler.postDelayed(this, 1000)
            }
        }
        curTimeHandler.post(updateTimeRunnable)
    }

    // 持久化
    private fun saveSignInfo(account: String, password: String, md5Pwd: String) {
        spUtils?.getInstance()?.putString(ConstantLogic.SP_SAVE_SIGN_ACCOUNT, account)
        spUtils?.getInstance()?.putString(ConstantLogic.SP_SAVE_SIGN_PWD, password)
        spUtils?.getInstance()?.putString(ConstantLogic.SP_SAVE_SIGN_MD5_PWD, md5Pwd)
    }

    // 登录
    private var loginRetryCount: Int = 1
    fun signIn(account: String, password: String) {
        val md5Password = RetrofitManager.instance.getMd5ByPwd(password)
        saveSignInfo(account, password, md5Password)
        RetrofitManager.instance.setWuHanUserName(account)
        RetrofitManager.instance.setWuHanPassWord(md5Password)
        firstLogin()
    }

    private fun firstLogin() {
        RetrofitManager.instance.firstLogin()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {
                    addSubscription(d)
                }

                override fun onNext(body: ResponseBody) {
                    RetrofitManager.instance.parseLoginResponseBody(body)
                }

                override fun onError(e: Throwable) {
                    if (loginRetryCount <= 5) {
                        ThreadUtils.getMainHandler().postDelayed({
                            firstLogin()
                        }, 2000)
                    } else {
                        MyLog.e(TAG, " init err count: $loginRetryCount")
                    }
                    loginRetryCount++
                    MyLog.w(TAG,"init onError: ${e.message}")
                }

                override fun onComplete() {
                    RetrofitManager.instance.init()
                    RetrofitManager.instance.getRobotsForHome()
                    view.showHome()
                }
            })
    }

    // 获取首页需要的数据
    fun getHomeData(): DealResult {
        val dealResult = DealResult()
        dealResult.type = ConstantLogic.MSG_TYPE_HOME
        dealResult.robotInfoList = RetrofitManager.instance.getRobotData()
        return dealResult
    }

    // 设置默认服务器
    private fun setDefaultServer() {
        if (spUtils?.getInstance()?.getString(ConstantLogic.SP_BIND_SERVER).isNullOrEmpty()) {
            spUtils?.getInstance()?.putString(ConstantLogic.SP_BIND_SERVER, NetConstant.DEFAULT_SERVICE_URL)
        }
        spUtils?.getInstance()?.getString(ConstantLogic.SP_BIND_SERVER)
            ?.let { RetrofitManager.instance.setDefaultServer(it) }
    }
}