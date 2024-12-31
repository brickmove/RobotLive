package com.ciot.robotlive.network.tcp

/**
 * Created by p'c on 2024/7/8.
 * Description:
 * Encoding: utf-8
 */
interface TcpClientListener {
    fun onMessageReceived(message: ByteArray)
}