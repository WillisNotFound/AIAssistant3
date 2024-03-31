package com.willis.ernie

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class ChatRequestBody(
    @SerializedName("messages")
    val messages: List<ErnieQuestion>,
    @SerializedName("temperature")
    val temperature: Float
)