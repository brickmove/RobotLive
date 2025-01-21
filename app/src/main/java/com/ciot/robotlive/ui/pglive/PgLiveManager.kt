package com.ciot.robotlive.ui.pglive

import android.view.SurfaceView
import android.view.View
import android.widget.LinearLayout
import com.ciot.robotlive.constant.NetConstant
import com.ciot.robotlive.utils.ContextUtil
import com.ciot.robotlive.utils.MyLog
import com.google.gson.Gson
import com.peergine.android.livemulti.pgLibLiveMultiError
import com.peergine.android.livemulti.pgLibLiveMultiRender
import com.peergine.android.livemulti.pgLibLiveMultiView
import com.peergine.plugin.lib.pgLibJNINode
import java.util.concurrent.TimeUnit


/**
 * Created by p'c on 2025/1/8.
 * Description:
 * Encoding: utf-8
 */
class PgLiveManager {
    companion object {
        private var TAG = "PgLiveManager"
        private var TAG_EVENT = "PgLiveManager_Event"
        private var MAX_VIEW_NUM = 4
        private var CLIENT_TYPE = 1
        val instance: PgLiveManager
            get() = RenderViewHolder.holder
    }

    private object RenderViewHolder {
        val holder = PgLiveManager()
    }

    private lateinit var mLive: pgLibLiveMultiRender
    private lateinit var mListStream : StreamInfo
    private var mInit = false
    class StreamInfo {
        var bConnect: Boolean = false
        var sDevID: String = ""
        var sVID: String = "0"
        var iVideoID: Int = 0
        var iAudioID: Int = 0
        var iView: LinearLayout? = null
        var iWnd: SurfaceView? = null
    }

    private fun checkPgLivePlugin(): Boolean {
        if (pgLibJNINode.Initialize(ContextUtil.getContext())) {
            pgLibJNINode.Clean()
            return true
        } else {
            MyLog.e(TAG, "Please import 'pgPluginLib' peergine middle ware!")
            return false
        }
    }

    private val mOnEvent =
        pgLibLiveMultiRender.OnEventListener { sAct, sData, sCapID ->
            var sInfo = ""
            when (sAct) {
                "VideoStatus" -> {

                }
                "Notify" -> {

                }
                "Message" -> {

                }
                "Login" -> {
                    sInfo = "Login success"
                    if (sData != "0") {
                        sInfo = "Login failed, error=$sData"
                    }
                }
                "Logout" -> {
                    sInfo = "Logout"
                }
                "Connect" -> {
                    sInfo = "Connect to capture"
                }
                "Disconnect" -> {
                    sInfo = "Disconnect from capture"
                }
                "Reject" -> {
                    sInfo = "Reject by capture"
                }
                "Offline" -> {
                    sInfo = "Capture offline"
                }
                "LanScanResult" -> {
                    sInfo = "Lan scan result: $sData"
                }
                "RecordStopVideo" -> {
                    sInfo = "Record stop audio: $sData"
                }
                "RecordStopAudio" -> {
                    sInfo = "Record stop audio: $sData"
                }
                "ForwardAllocReply" -> {
                    sInfo = "Forward alloc reply: error=$sData"
                }
                "ForwardFreeReply" -> {
                    sInfo = "Forward free relpy: error=$sData"
                }
                "VideoCamera" -> {
                    sInfo = "The picture is save to: $sData"
                }
                "SvrNotify" -> {
                    sInfo = "Receive server notify: $sData"
                }
                "PeerInfo" -> {
                    sInfo = "PeerInfo: $sData"
                }
                else -> {
                    MyLog.d(TAG_EVENT, "no act match: $sAct")
                }

            }
            //ToastUtil.showToast(0, ContextUtil.getContext(), sInfo)
            MyLog.d(
                TAG_EVENT, "OnEvent: Act=$sAct, Data=$sData, sCapID=$sCapID"
            )
        }

    fun initView(view: LinearLayout) {
        MyLog.d(TAG, "initView....")
        if (!checkPgLivePlugin() || mInit) {
            return
        }

        mListStream = StreamInfo()
        mLive = pgLibLiveMultiRender()
        mLive.SetEventListener(mOnEvent)
        liveLogin(view)
        mInit = true
    }

    private fun liveLogin(view: LinearLayout) {
        val sInitParam = "(Debug){1}(VideoSoftDecode){1}"
        val iErr = mLive.Initialize(NetConstant.PG_LIVE_PEER_USER, NetConstant.PG_LIVE_PEER_PWD,
            NetConstant.PG_LIVE_SERVER_ADDRESS, NetConstant.PG_LIVE_REPLAY_ADDRESS,
            1, sInitParam, ContextUtil.getContext())
        if (iErr != pgLibLiveMultiError.PG_ERR_Normal) {
            MyLog.d(TAG, "LiveStart: Live.Initialize failed! iErr=$iErr")
            return
        }

        val sViewID = ("view0")
        mListStream.iWnd = pgLibLiveMultiView.Get(sViewID)
        mListStream.iView = view
        mListStream.iView!!.addView(mListStream.iWnd)
        mListStream.iWnd!!.visibility = View.VISIBLE
    }

    fun liveLogout() {
        MyLog.d(TAG, "liveLogout.....mInit=$mInit")
        if (!mInit) {
            return
        }
        mInit = false
        liveDisconnect()
        mListStream.iView?.removeView(mListStream.iWnd)
        pgLibLiveMultiView.Release(mListStream.iWnd)
        mListStream.iView = null
        mListStream.iWnd = null
        mLive.Clean()
    }

    private fun liveDisconnect() {
        mLive.VideoStop(mListStream.sDevID, mListStream.iVideoID)
        mLive.AudioStop(mListStream.sDevID, mListStream.iAudioID)
        mListStream.bConnect = false
        mLive.Disconnect(mListStream.sDevID)
        mListStream.sDevID = ""
        mListStream.iVideoID = 0
        mListStream.iAudioID = 0
    }

    fun liveConnect(id: String, videoId: Int, audioId: Int) {
        if (mListStream.bConnect) {
            MyLog.d(TAG, "The stream is exist!")
            return
        }
        MyLog.d(TAG, "liveConnect: id=$id, videoId=$videoId, audioId=$audioId")
        mListStream.sDevID = id
        mListStream.iVideoID = videoId
        //mListStream.iAudioID = audioId
        if (mLive.Connect(mListStream.sDevID) != pgLibLiveMultiError.PG_ERR_Normal) {
            return
        }

        mListStream.bConnect = true
        mLive.VideoStart(mListStream.sDevID, mListStream.iVideoID, "", mListStream.iWnd)

        //val sAudioParam = ""
        //mLive.AudioStart(mListStream.sDevID, mListStream.iAudioID, sAudioParam)
        //mLive.AudioSyncDelay(mListStream.sDevID, mListStream.iAudioID, mListStream.iVideoID)
    }

    // 静音操作
    fun liveAudioMute(bInput: Boolean, bOutput: Boolean) {
        mLive.AudioMute(mListStream.sDevID, mListStream.iAudioID, bInput, bOutput)
    }

    // 开启音频
    fun startLiveAudio(audioId: Int, client: String?) {
        val sAudioParam = ""
        mListStream.iAudioID = audioId
        MyLog.d(TAG, "startLiveAudio sDevID=${mListStream.sDevID}, iAudioID=${mListStream.iAudioID}")
        mLive.AudioStart(mListStream.sDevID, mListStream.iAudioID, sAudioParam)

        if (!client.isNullOrEmpty()) {
            sendLiveMsg(client, CLIENT_TYPE)
        }
    }

    // 发送客户端消息
    private fun sendLiveMsg(client: String, clientType: Int) {
        val clientBean = ClientBean(
            client = client,
            clientType = clientType,
            audioId = mListStream.iAudioID,
            videoId = mListStream.iVideoID
        )
        val data = Gson().toJson(clientBean)

        var attempts = 0
        val maxRetries = 100
        var ret = -1
        while (attempts < maxRetries) {
            ret = mLive.MessageSend(mListStream.sDevID, data)
            MyLog.d(TAG, "sendLiveMsg ret=$ret, attempts=$attempts")
            if (ret == 0) {
                break
            } else {
                attempts++
                TimeUnit.MILLISECONDS.sleep(1000)
            }
        }

        //val ret = mLive.MessageSend("_CAP_YHPR1150B002SZGM202101140172", "Message")
        MyLog.d(TAG, "sendLiveMsg dev id: ${mListStream.sDevID}, ret: $ret")
    }
}