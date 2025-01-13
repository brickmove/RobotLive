package com.ciot.robotlive.bean

/**
 * Created by p'c on 2024/6/7.
 * Description:
 * Encoding: utf-8
 */
class DealResult {
    var type: Int = 0

    // 界面数据设置
    var selectRobotId: String? = null
    var videoCode: String? = null
    var client: String? = null
    var robotList: List<String>? = null
    var robotInfoList: List<RobotData>? = null

    // 是否震动
    var isVibratorOn: Boolean = false
    override fun toString(): String {
        return "DealResult(type=$type, " +
                "selectRobotId, $selectRobotId" +
                "videoCode, $videoCode" +
                "robotList = $robotList, " +
                "robotInfoList = $robotInfoList, " +
                ")"
    }
}