package com.ciot.robotlive.bean
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Created by p'c on 2024/7/8.
 * Description:
 * Encoding: utf-8
 */
class HeartBeatBeanR {
    @Expose(serialize = true, deserialize = true)
    @SerializedName("id")
    var id: String? = null

    @Expose(serialize = true, deserialize = true)
    @SerializedName("time")
    var time: Long? = null


}