package com.ciot.robotlive.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi

object VibrationUtils {
    private val lock = Object()

    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrate(context: Context, duration: Long) {
        if (hasVibratorFeature(context)) {
            synchronized(lock) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        }
    }

    fun cancelVibration(context: Context) {
        if (hasVibratorFeature(context)) {
            synchronized(lock) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.cancel()
            }
        }
    }

    fun hasVibratorFeature(context: Context): Boolean {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return vibrator.hasVibrator()
    }
}
