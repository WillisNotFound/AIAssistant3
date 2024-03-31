package com.willis.qwen

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
internal class ChatRequestBody(
    @SerializedName("model")
    val model: String,
    @SerializedName("input")
    val input: Input,
    @SerializedName("parameters")
    val parameters: Parameters
) {
    class Input(
        @SerializedName("messages")
        val messages: List<QwenQuestion>
    )

    class Parameters(
        @SerializedName("result_format")
        val resultFormat: String,
        @SerializedName("temperature")
        val temperature: Float
    )
}