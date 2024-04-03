package com.willis.qwen

import com.willis.base.data.BaseResult
import com.willis.base.services.httpService
import com.willis.base.utils.GsonUtils
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


/**
 * description: 阿里通义千问大模型, https://dashscope.console.aliyun.com/billing
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
object Qwen {
    private const val URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation"

    private const val MODEL_QWEN_TURBO = "qwen-turbo"
    private const val MODEL_QWEN_PLUS = "qwen-plus"
    private const val MODEL_QWEN_MAX = "qwen-max"
    private const val MODEL_QWEN_MAX_LONG_CONTEXT = "qwen-max-longcontext"
    private const val MODEL_QWEN_1_8B_CHAT = "qwen-1.8b-chat"

    suspend fun sendMessage(
        apiKey: String,
        model: String,
        temperature: Float,
        enableSearch: Boolean,
        messages: List<QwenMessage>
    ): BaseResult<String> {
        val request = buildSendMessageRequest(apiKey, model, temperature, enableSearch, messages)
        return try {
            val response = httpService.send(request)
            when (val result = parseSendMessageResponse(response)) {
                is BaseResult.Success -> BaseResult.Success(result.desc, result.value.output.text)
                is BaseResult.Failure -> BaseResult.Failure(result.desc)
            }
        } catch (e: Exception) {
            BaseResult.Failure(e.message ?: e.toString())
        }
    }

    private fun buildSendMessageRequest(
        apiKey: String,
        model: String,
        temperature: Float,
        enableSearch: Boolean,
        messages: List<QwenMessage>
    ) = Request.Builder()
        .url(URL)
        .method(
            "POST", GsonUtils.toJson(
                RequestBody(
                    model = model,
                    input = RequestBody.Input(messages),
                    parameters = RequestBody.Parameters("text", temperature, enableSearch)
                )
            ).toRequestBody()
        )
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", apiKey)
        .build()

    private fun parseSendMessageResponse(response: Response): BaseResult<ResponseBody> {
        val body = response.body?.string() ?: ""
        val jsonObject = GsonUtils.fromJson(body)
        return if (jsonObject["code"] != null) {
            BaseResult.Failure(jsonObject["message"].asString)
        } else if (jsonObject["output"] != null) {
            BaseResult.Success("请求成功", GsonUtils.fromJson(body, ResponseBody::class.java))
        } else {
            val error = "未知错误：code: ${response.code}, msg: ${response.message}"
            BaseResult.Failure(error)
        }
    }
}