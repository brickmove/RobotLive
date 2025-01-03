package com.ciot.robotlive.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

// 使用对象表达式实现单例模式
object SPUtils {

    private const val TAG = "PrefManager"
    private const val MAX_RETRIES = 3
    private const val DEFAULT_SP_NAME = "DELIVERY_SP_DATA"

    // 获取 SharedPreferences 实例
    private val pref: SharedPreferences by lazy {
        ContextUtil.getContext().getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 泛型方法用于存储各种类型的值
     */
    private inline fun <reified T> put(key: String, value: T, isCommit: Boolean) {
        if (isCommit) {
            var success = false
            var attempts = 0
            while (!success && attempts < MAX_RETRIES) {
                pref.edit(commit = true) {
                    when (value) {
                        is Boolean -> putBoolean(key, value)
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        else -> throw IllegalArgumentException("Unsupported type: ${value!!::class.java}")
                    }
                }
                success = pref.contains(key) && pref.all[key] == value
                if (success) {
                    MyLog.d(TAG, "Set value to $value successfully")
                } else {
                    MyLog.d(TAG, "Failed to set value to $value. Attempt ${attempts + 1} of $MAX_RETRIES")
                }
                attempts++
            }
        } else {
            pref.edit {
                when (value) {
                    is Boolean -> putBoolean(key, value)
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    else -> throw IllegalArgumentException("Unsupported type: ${value!!::class.java}")
                }
            }
        }
    }

    // 存储布尔值
    fun putBoolean(key: String, value: Boolean, isCommit: Boolean = false) =
        put(key, value, isCommit)

    // 获取布尔值
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        pref.getBoolean(key, defaultValue)

    // 存储字符串
    fun putString(key: String, value: String, isCommit: Boolean = false) =
        put(key, value, isCommit)

    // 获取字符串
    fun getString(key: String, defaultValue: String = ""): String? =
        pref.getString(key, defaultValue)

    // 存储整数
    fun putInt(key: String, value: Int, isCommit: Boolean = false) =
        put(key, value, isCommit)

    // 获取整数
    fun getInt(key: String, defaultValue: Int = -1): Int =
        pref.getInt(key, defaultValue)

    // 检查是否存在键
    fun contains(key: String): Boolean =
        pref.contains(key)
}