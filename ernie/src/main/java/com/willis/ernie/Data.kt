package com.willis.ernie

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
internal class RequestBody(
    @SerializedName("messages")
    val messages: List<ErnieMessage>,
    @SerializedName("temperature")
    val temperature: Float
)

class ResponseBody(
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


class ErnieMessage(
    @SerializedName("role")
    val role: String,// 用户 - user, 机器人 - assistant
    @SerializedName("content")
    val content: String
)