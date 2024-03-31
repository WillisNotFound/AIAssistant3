package com.willis.qwen

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class ChatResponseBody(
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