package com.ciot.robotlive.bean

class StartPlayResponse {
    // 流标识，（对每次调用启动返回唯一标识）
    var handler: String? = null

    var height: Int? = null
    var width: Int? = null
    var iMode: Int? = null
    var result: Boolean? = null

    // 流ID（流ID（穿透SDK流ID，客户端需要使用））
    var stream: Int? = null

}
