package com.willis.qwen

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
internal class RequestBody(
    @SerializedName("model")
    val model: String,
    @SerializedName("input")
    val input: Input,
    @SerializedName("parameters")
    val parameters: Parameters
) {
    class Input(
        @SerializedName("messages")
        val messages: List<QwenMessage>
    )

    class Parameters(
        @SerializedName("result_format")
        val resultFormat: String,
        @SerializedName("temperature")
        val temperature: Float,
        @SerializedName("enable_search")
        val enableSearch: Boolean
    )
}

class ResponseBody(
    @SerializedName("output")
    val output: Output,
    @SerializedName("usage")
    val usage: Usage,
    @SerializedName("request_id")
    val requestId: String
) {
    class Output(
        @SerializedName("finish_reason")
        val finishReason: String,
        @SerializedName("text")
        val text: String
    )

    class Usage(
        @SerializedName("total_tokens")
        val totalTokens: Int,
        @SerializedName("output_tokens")
        val outputTokens: Int,
        @SerializedName("input_tokens")
        val inputTokens: Int
    )
}

class QwenMessage(
    @SerializedName("role")
    val role: String,// 用户 - user, 机器人 - assistant
    @SerializedName("content")
    val content: String
)

