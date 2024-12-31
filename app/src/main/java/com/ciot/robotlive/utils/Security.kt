package com.ciot.robotlive.utils
import java.security.MessageDigest

// 加密操作处理
class Security {
    companion object {
        @Volatile
        private var instance: Security? = null
        fun getInstance(): Security {
            if (instance == null) {
                synchronized(Security::class.java) {
                    if (instance == null) {
                        instance = Security()
                    }
                }
            }
            return instance!!
        }
    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        // 对字符串进行 MD5 加密
        val hashBytes = md.digest(this.toByteArray(Charsets.UTF_8))
        // 将字节数组转换为十六进制字符串
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun getMd5ValueFromPassword(pwd: String): String {
        return pwd.md5()
    }

}