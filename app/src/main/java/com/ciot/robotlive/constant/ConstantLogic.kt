package com.ciot.robotlive.constant

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface ConstantLogic {
    companion object {
        // 日志TAG标签
        const val TIME_TEST = "TIME_TEST"
        const val FRAGMENT = "FRAGMENT"
        const val NETWORK_TAG = "NETWORK_TAG"
        const val TCP_TAG = "TCP_TAG"
        const val HTTP_TAG = "HTTP_TAG"

        // fragment
        const val MSG_TYPE_HOME = 0 // 主页
        const val MSG_TYPE_SIGN = 1 // 登录页面
        const val MSG_TYPE_LIVE = 2 // 直播界面

        // Event bus constant
        const val EVENT_ARRIVED_POINT = "EVENT_ARRIVED_POINT" // 到达点位
        const val EVENT_SHOW_HOME = "EVENT_SHOW_HOME"         // 显示首页
        const val EVENT_REFRESH_HOME = "EVENT_REFRESH_HOME"   // 刷新首页
        const val EVENT_RECONNECT_TCP = "EVENT_RECONNECT_TCP" // 重连tcp

        // SharedPreferences val
        const val SP_BIND_SERVER = "SP_BIND_SERVER"
        const val SP_IS_SIGNED_IN = "SP_IS_SIGNED_IN"
        const val SP_SAVE_SIGN_ACCOUNT = "SP_SAVE_SIGN_ACCOUNT"
        const val SP_SAVE_SIGN_PWD = "SP_SAVE_SIGN_PWD"
        const val SP_SAVE_SIGN_MD5_PWD = "SP_SAVE_SIGN_MD5_PWD"

        // file constant
        val APP_PATH: String = Environment.getExternalStorageDirectory().toString() + File.separator + "RobotLog"
        val LOG_TODAY_FILE_NAME: String = SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date())
        val Dir_LOG_TODAY: String = APP_PATH + File.separator + LOG_TODAY_FILE_NAME

        // 登录字符限制
        val accountLen = 8
        val pwdLen = 9
    }
}