package com.ciot.robotlive.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.GsonUtils
import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.bean.StartPlayResponse
import com.ciot.robotlive.databinding.FragmentLiveBinding
import com.ciot.robotlive.network.RetrofitManager
import com.ciot.robotlive.ui.base.BaseFragment
import com.ciot.robotlive.ui.pglive.PgLiveManager
import com.ciot.robotlive.ui.viewmodel.CountdownViewModel
import com.ciot.robotlive.ui.widgets.DirectionFourKey
import com.ciot.robotlive.utils.MyLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LiveFragment : BaseFragment() {
    companion object {
        private const val TAG = "LiveFragment"
        private const val UP = "forward"
        private const val DOWN = "backward"
        // 客户要求左右对调
        private const val LEFT = "right"
        private const val RIGHT = "left"
    }
    private lateinit var binding : FragmentLiveBinding
    private var wvLive: LinearLayout? = null
    private var tvRemainTime: TextView? = null
    private var btRecharge: Button? = null
    private var ibVoiceInput: ImageButton? = null
    private var ibVoiceOutput: ImageButton? = null
    private var directionFourKey: DirectionFourKey? = null
    private var mRemainTime: String = "12:00:00"
    private lateinit var countdownViewModel: CountdownViewModel
    private var mRobotId: String? = null
    private var mVideoCode: String? = null
    private var mChannel: Int? = null
    private var savedState: Bundle? = null

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        binding = FragmentLiveBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            savedState = savedInstanceState
        }
        init()
    }

    private fun init() {
        initView()
        initViewModel()
        initListener()
        setupCountdown()
    }

    private fun initView() {
        wvLive = binding.wvLive
        tvRemainTime = binding.tvRemainTime
        btRecharge = binding.btRecharge
        ibVoiceInput = binding.btVoiceInput
        ibVoiceOutput = binding.btVoiceOutput
        directionFourKey = binding.includeDirectionControl.mainDirectionFourRv
        PgLiveManager.instance.initView(wvLive!!)
    }

    private fun initViewModel() {
        countdownViewModel = ViewModelProvider(this)[CountdownViewModel::class.java]
    }

    private fun initListener() {
        countdownViewModel.countdownStateMap.observe(viewLifecycleOwner, Observer { states ->
            val state = states[mRobotId]
            state?.let {
                updateCountdownText(it.remainingTime)
            }
        })
        directionFourKey?.setOnDirectionListener(onDirectionListener)
    }

    private fun updateCountdownText(millisUntilFinished: Long) {
        //MyLog.d(TAG, "updateCountdownText: $millisUntilFinished")
        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))

        val timeString = String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        )
        tvRemainTime?.text = timeString
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 保存当前剩余时间
        val state = countdownViewModel.countdownStateMap.value?.get(mRobotId)
        state?.let {
            outState.putLong("remaining_time_$mRobotId", it.remainingTime)
            outState.putBoolean("is_paused_$mRobotId", it.isPaused)
        }
    }

    override fun refreshData(isRefreshImmediately: Boolean, data: DealResult) {
        super.refreshData(isRefreshImmediately, data)
        mRobotId = data.selectRobotId
        mVideoCode = data.videoCode
        MyLog.d(TAG, "refreshData mRobotId: $mRobotId, mVideoCode: $mVideoCode")
        setupCountdown()
        mVideoCode?.let { PgLiveManager.instance.liveConnect(it, 1, 1) }
        startLiveVoice(data)
        //PgLiveManager.instance.startLiveAudio(9)
    }

    private fun startLiveVoice(dealResult: DealResult) {
        val channel = 9
        val stream = 9
        val source = 888888
        RetrofitManager.instance.startVoice(dealResult.selectRobotId!!, channel, dealResult.client!!, stream, source)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object: io.reactivex.Observer<StartPlayResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(body: StartPlayResponse) {
                    MyLog.d(TAG, "startLiveVoice: " + GsonUtils.toJson(body))
                    RetrofitManager.instance.parseVoiceResponseBody(body)
                }

                override fun onError(e: Throwable) {
                    MyLog.e(TAG,"startLiveVoice onError: ${e.message}")
                }

                override fun onComplete() {
                    PgLiveManager.instance.startLiveAudio(stream, dealResult.client)
                }
            })
    }

    private fun setupCountdown() {
        // 检查是否有保存的实例状态（例如，从 Bundle 恢复）
        if (savedState != null) {
            MyLog.d(TAG, "checkState1")
            val savedTime = savedState!!.getLong("remaining_time_$mRobotId")
            val isPaused = savedState!!.getBoolean("is_paused_$mRobotId")
            if (!isPaused) {
                mRobotId?.let { countdownViewModel.resumeCountdown(it) }
            } else {
                mRobotId?.let { countdownViewModel.startCountdown(it, savedTime) }
            }
        } else {
            MyLog.d(TAG, "checkState2: $mRobotId")
            // 使用辅助函数将 mRemainTime 字符串转换为秒数
            val totalSeconds = parseTimeStringToSeconds(mRemainTime)
            // 将秒数转换为毫秒数
            val startTimeMillis = TimeUnit.SECONDS.toMillis(totalSeconds)
            // 启动倒计时
            mRobotId?.let { countdownViewModel.startCountdown(it, startTimeMillis) }
        }
    }

    private fun parseTimeStringToSeconds(timeString: String): Long {
        val parts = timeString.split(":").map { it.toLong() }
        if (parts.size != 3) throw IllegalArgumentException("Invalid time format")

        val hours = parts[0]
        val minutes = parts[1]
        val seconds = parts[2]

        return hours * 3600 + minutes * 60 + seconds
    }

    //四建摇杆控制
    private val onDirectionListener: DirectionFourKey.OnDirectionListener = object :
        DirectionFourKey.OnDirectionListener {
            override fun upUp() {
                mRobotId?.let { RetrofitManager.instance.stopMove(it, UP) }
            }

            override fun downUp() {
                mRobotId?.let { RetrofitManager.instance.stopMove(it, DOWN) }
            }

            override fun leftUp() {
                mRobotId?.let { RetrofitManager.instance.stopMove(it, LEFT) }
            }

            override fun rightUp() {
                mRobotId?.let { RetrofitManager.instance.stopMove(it, RIGHT) }
            }

            override fun upDown() {
                mRobotId?.let { RetrofitManager.instance.startMove(it, UP) }
            }

            override fun downDown() {
                mRobotId?.let { RetrofitManager.instance.startMove(it, DOWN) }
            }

            override fun leftDown() {
                mRobotId?.let { RetrofitManager.instance.startMove(it, LEFT) }
            }

            override fun rightDown() {
                mRobotId?.let { RetrofitManager.instance.startMove(it, RIGHT) }
            }
    }

    private fun stopLive() {
        PgLiveManager.instance.liveLogout()
        if (!mRobotId.isNullOrEmpty()) {
            RetrofitManager.instance.stopLive(mRobotId!!)
            RetrofitManager.instance.stopVoice(mRobotId!!)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            stopLive()
        } else {
            PgLiveManager.instance.initView(wvLive!!)
        }
    }
}
