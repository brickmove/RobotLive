package com.ciot.robotlive.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 * Created by p'c on 2024/7/17.
 * Description:
 * Encoding: utf-8
 */
object ToastUtil {
    private var isToast: Boolean = true
    private var mToast: Toast? = null

    fun showLong(context: Context?, msg: String?) {
        if (isToast) {
            val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 10)
            toast.show()
        }
    }

    fun showShort(context: Context?, msg: String?) {
        if (isToast) {
            val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 10)
            toast.show()
        }
    }

    fun showToast(lengthShort: Int, context: Context?, msg: String?) {
        if (lengthShort == Toast.LENGTH_LONG) {
            showLong(context, msg)
        } else {
            showShort(context, msg)
        }
    }

    fun cancelToast() {
        mToast?.cancel()
    }
}