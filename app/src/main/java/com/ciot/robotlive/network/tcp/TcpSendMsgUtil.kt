package com.ciot.robotlive.network.tcp

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.ciot.robotlive.bean.HeartBeatBeanR
import com.ciot.robotlive.bean.ProtocolBean
import com.ciot.robotlive.bean.RegisterBeanR
import com.ciot.robotlive.constant.ConstantLogic
import com.ciot.robotlive.constant.NetConstant
import com.ciot.robotlive.network.RetrofitManager
import com.ciot.robotlive.utils.MyLog
import java.util.Date

/**
 * Created by p'c on 2024/7/8.
 * Description: 拼装发送命令
 * Encoding: utf-8
 */
class TcpSendMsgUtil {
    private var TAG = ConstantLogic.TCP_TAG
    private var mSeq: Int = 1
    private var type: Int = 101

    /**
     * 获取Seq值，累加，大于最大值时，进行重置为1
     *
     * @return seq
     */
    private fun getSeq(): Int {
        if (mSeq >= Int.MAX_VALUE) {
            mSeq = 1
        } else {
            mSeq++
        }
        return mSeq
    }

    private fun getProtocolBean(): ProtocolBean {
        val protocolBean = ProtocolBean()
        protocolBean.ver = 0x02
        protocolBean.cflag = 0x00
        protocolBean.rflag = 0x00
        protocolBean.qa = 0x00
        protocolBean.type = 101
        return protocolBean
    }

    private object TcpSendMsgUtilHolder {
        val holder = TcpSendMsgUtil()
    }

    companion object {
        val instance: TcpSendMsgUtil
            get() = TcpSendMsgUtilHolder.holder
    }

    /**
     * 注册
     */
    fun sendRegisterWatch(): ProtocolBean {
        val register = RegisterBeanR()
        //register.id = MyDeviceUtils.getMacAddress()
        register.id = RetrofitManager.instance.getWuHanUserName()
        register.type = type
        register.version = AppUtils.getAppVersionName()
        register.time = (Date().time / 1000).toString()
        val protocolBean = getProtocolBean()
        protocolBean.seq = getSeq()
        protocolBean.cmd = NetConstant.CONTROL_DEVICE_MANAGEMENT_REGISTER
        protocolBean.body = register
        MyLog.d(TAG, "发送注册平台消息: " + GsonUtils.toJson(protocolBean))
        return protocolBean
    }

    /**
     * 发送心跳包
     */
    fun sendHeartBeat(): ProtocolBean {
        val hearBeat = HeartBeatBeanR()
        //hearBeat.id = MyDeviceUtils.getMacAddress()
        hearBeat.id = RetrofitManager.instance.getWuHanUserName()
        hearBeat.time = Date().time
        val protocolBean = getProtocolBean()
        protocolBean.seq = getSeq()
        protocolBean.cmd = NetConstant.CONTROL_STATUS_HEART_BEAT
        protocolBean.body = hearBeat
        if (protocolBean.seq % 10 == 0) {
            MyLog.v(TAG, "sendHeartBeat: " + GsonUtils.toJson(protocolBean))
        }
        return protocolBean
    }
}