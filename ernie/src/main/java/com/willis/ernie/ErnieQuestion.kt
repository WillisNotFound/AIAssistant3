package com.willis.ernie

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class ErnieQuestion(
    @SerializedName("role")
    val role: String,// 用户 - user, 机器人 - assistant
    @SerializedName("content")
    val content: String
)