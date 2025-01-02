package com.ciot.robotlive

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.constant.ConstantLogic
import com.ciot.robotlive.databinding.ActivityMainBinding
import com.ciot.robotlive.ui.base.BaseActivity
import com.ciot.robotlive.ui.base.BaseFragment
import com.ciot.robotlive.ui.fragment.ControlFragment
import com.ciot.robotlive.ui.fragment.FragmentFactory
import com.ciot.robotlive.ui.fragment.HomeFragment
import com.ciot.robotlive.utils.MyLog
import com.ciot.robotlive.utils.ToastUtil
import java.util.LinkedList

class MainActivity : BaseActivity<MainPresenter>(), MainView, View.OnClickListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var binding : ActivityMainBinding
    private var currentfragment: BaseFragment? = null
    private var showingFragment: Fragment? = null

    override fun createPresent(): MainPresenter {
        return MainPresenter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyLog.d(TAG, "MainActivity onCreate start")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mPresenter?.initSetting()
    }

    override fun onResume() {
        super.onResume()
        initListener()

    }

    override fun update(results: LinkedList<out DealResult>?) {
        TODO("Not yet implemented")
    }

    override fun getCurrentFragment(): BaseFragment? {
        return currentfragment
    }

    override fun showToast(updateText: String) {
        runOnUiThread { ToastUtil.showToast(0,this@MainActivity, updateText) }
    }

    fun showSign() {
        MyLog.d(TAG, "MainActivity showSign >>>>>>>>>")
        updateFragment(ConstantLogic.MSG_TYPE_SIGN, null)
    }

    fun showHome() {
        MyLog.d(TAG, "MainActivity showHome >>>>>>>>>")
        updateFragment(ConstantLogic.MSG_TYPE_HOME, mPresenter?.getHomeData())
    }

    private fun initListener() {
        binding.headView.ivReturn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.headView.ivReturn -> {
                if (currentfragment is HomeFragment) {
                    showSign()
                } else if (currentfragment is ControlFragment) {
                    showHome()
                }
            }
        }
    }

    private fun updateFragment(type: Int, result: DealResult?) {
        getFragment(type)
        changeFragment(type, currentfragment)
        setHeadView(type)
        if (result != null) {
            currentfragment?.refreshData(false, result)
        }
    }

    private fun getFragment(type: Int) {
        currentfragment = FragmentFactory.createFragment(type)
    }

    private fun changeFragment(type: Int, newFragment: Fragment?) {
        if (newFragment == null) {
            MyLog.d(TAG, "changeFragment current fragment == null")
            return
        }

        if (newFragment == showingFragment) {
            MyLog.d(TAG, "changeFragment newFragment->${newFragment.javaClass.simpleName} == showingFragment>${showingFragment?.javaClass?.simpleName}")
            return
        }

        val containerView: ViewGroup
        if (type == ConstantLogic.MSG_TYPE_HOME
            || type == ConstantLogic.MSG_TYPE_SIGN
        ) {
            binding.containerMain.visibility = View.VISIBLE
            containerView = binding.containerMain
            binding.containerFull.visibility = View.GONE
        } else {
            binding.containerFull.visibility = View.VISIBLE
            containerView = binding.containerFull
            binding.containerMain.visibility = View.GONE
        }
        FragmentFactory.changeFragment(supportFragmentManager, containerView, newFragment)
        showingFragment = newFragment
    }

    private fun setHeadView(type: Int) {
        when (type) {
            ConstantLogic.MSG_TYPE_HOME,
            ConstantLogic.MSG_TYPE_CONTROL-> {
                binding.headView.ivReturn.visibility = View.VISIBLE
                binding.headView.tvRobotControl.visibility = View.VISIBLE
                binding.headView.tvHome.visibility = View.GONE
            }
            ConstantLogic.MSG_TYPE_SIGN-> {
                binding.headView.ivReturn.visibility = View.GONE
                binding.headView.tvRobotControl.visibility = View.GONE
                binding.headView.tvHome.visibility = View.VISIBLE
            }
        }
    }

    fun signIn(account: String, password: String) {
        mPresenter?.signIn(account, password)
    }
}

