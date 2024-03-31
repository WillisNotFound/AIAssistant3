package com.willis.ernie

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class ChatResponseBody(
    @SerializedName("id")
    val id: String,
    @SerializedName("object")
    val _object: String,
    @SerializedName("create")
    val create: Int,
    @SerializedName("result")
    val result: String,
    @SerializedName("need_clear_history")
    val needClearHistory: Boolean
)