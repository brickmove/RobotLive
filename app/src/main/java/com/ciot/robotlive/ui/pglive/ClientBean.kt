package com.ciot.robotlive.ui.pglive

import com.google.gson.annotations.SerializedName

/**
 * Created by p'c on 2025/1/20.
 * Description:
 * Encoding: utf-8
 */
data class ClientBean(
    @SerializedName("client") val client: String,
    @SerializedName("clientType") val clientType: Int,
    @SerializedName("audioId") val audioId: Int,
    @SerializedName("videoId") val videoId: Int
)
