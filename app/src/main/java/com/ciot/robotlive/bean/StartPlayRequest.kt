package com.ciot.robotlive.bean

class StartPlayRequest {
    // robot id
    var id: String? = null

    // 摄像头通道编号，从1到4
    var channel: Int? = null

    // 客户端唯一标识
    var client: String? = null

    // 0主码流，1子码流，2 第三码流
    var mode: Int? = null
}
