package com.ciot.robotlive.ui.viewmodel

data class CountdownState(val id: String, var remainingTime: Long, var isPaused: Boolean = false)
