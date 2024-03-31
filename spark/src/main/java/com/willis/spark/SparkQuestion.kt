package com.willis.spark

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/27
 */
class SparkQuestion(
    @SerializedName("role")
    val role: String,// 用户 - user, 机器人 - assistant
    @SerializedName("content")
    val content: String
)