package com.ciot.robotlive.network.tcp

import com.blankj.utilcode.util.GsonUtils
import com.ciot.robotlive.constant.ConstantLogic
import com.ciot.robotlive.constant.NetConstant
import com.ciot.robotlive.network.RetrofitManager
import com.ciot.robotlive.utils.MyLog
import com.ciot.robotlive.network.tcp.TcpClientListener
import org.greenrobot.eventbus.EventBus

/**
 * Created by p'c on 2024/7/8.
 * Description: 接收服务端消息
 * Encoding: utf-8
 */
class TcpMsgListener: TcpClientListener {
    private var TAG = ConstantLogic.TCP_TAG
    override fun onMessageReceived(message: ByteArray) {
        // 解析并处理收到的消息

    }
}