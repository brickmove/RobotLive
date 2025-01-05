package com.ciot.robotlive.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.ConcurrentHashMap

class CountdownViewModel : ViewModel() {
    private val countdownStates = ConcurrentHashMap<String, CountdownState>()
    private val _countdownStateMap = MutableLiveData<Map<String, CountdownState>>()
    val countdownStateMap: LiveData<Map<String, CountdownState>> get() = _countdownStateMap

    private val timers = ConcurrentHashMap<String, CountDownTimer>()

    fun startCountdown(id: String, startTimeMillis: Long) {
        stopCountdown(id)

        val state = CountdownState(id, startTimeMillis)
        countdownStates[id] = state

        val countDownTimer = object : CountDownTimer(startTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                state.remainingTime = millisUntilFinished
                updateLiveData()
            }

            override fun onFinish() {
                state.remainingTime = 0
                state.isPaused = true
                updateLiveData()
            }
        }.start()

        timers[id] = countDownTimer
    }

    fun pauseCountdown(id: String): Boolean {
        val timer = timers.remove(id) ?: return false
        timer.cancel()
        countdownStates[id]?.isPaused = true
        updateLiveData()
        return true
    }

    fun resumeCountdown(id: String) {
        val state = countdownStates[id] ?: return
        if (state.isPaused && state.remainingTime > 0) {
            startCountdown(id, state.remainingTime)
            state.isPaused = false
        }
    }

    fun stopCountdown(id: String) {
        val timer = timers.remove(id)
        timer?.cancel()
        countdownStates.remove(id)
        updateLiveData()
    }

    private fun updateLiveData() {
        _countdownStateMap.value = countdownStates.toMap()
    }

    override fun onCleared() {
        super.onCleared()
        for (timer in timers.values) {
            timer.cancel()
        }
        timers.clear()
        countdownStates.clear()
    }
}