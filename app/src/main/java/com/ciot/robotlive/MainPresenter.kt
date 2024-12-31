package com.ciot.robotlive

import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.constant.ConstantLogic
import com.ciot.robotlive.constant.NetConstant
import com.ciot.robotlive.databinding.ActivityMainBinding
import com.ciot.robotlive.network.RetrofitManager
import com.ciot.robotlive.ui.base.BasePresenter
import com.ciot.robotlive.utils.SPUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedList
import java.util.Locale

/**
 * Created by p'c on 2024/12/30.
 * Description:
 * Encoding: utf-8
 */
class MainPresenter(view: MainActivity) : BasePresenter<MainView>() {
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

    fun initSetting(view: MainActivity) {
        setDefaultServer()
        getCurTime(view.binding)
        if (spUtils?.getInstance()?.getBoolean(ConstantLogic.IS_SIGNED_IN, true) == false) {
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
                binding.headView.timeTextView.text = timeFormat.format(Date())
                curTimeHandler.postDelayed(this, 1000)
            }
        }
        curTimeHandler.post(updateTimeRunnable)
    }

    // 登录
    fun signIn(account: String, password: String) {
        val md5Password = RetrofitManager.instance.getMd5ByPwd(password)
        RetrofitManager.instance.setWuHanUserName(account)
        RetrofitManager.instance.setWuHanPassWord(md5Password)
    }

    // 设置默认服务器
    fun setDefaultServer() {
        if (spUtils?.getInstance()?.getString(ConstantLogic.BIND_SERVER).isNullOrEmpty()) {
            spUtils?.getInstance()?.putString(ConstantLogic.BIND_SERVER, NetConstant.DEFAULT_SERVICE_URL)
        }
        spUtils?.getInstance()?.getString(ConstantLogic.BIND_SERVER)
            ?.let { RetrofitManager.instance.setDefaultServer(it) }
    }
}