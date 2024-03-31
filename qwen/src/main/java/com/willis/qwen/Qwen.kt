package com.willis.qwen

import com.willis.base.data.BaseResult
import com.willis.base.services.httpService
import com.willis.base.utils.GsonUtils
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


/**
 * description: 阿里通义千问大模型
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
object Qwen {
    // 可用模型列表：https://dashscope.console.aliyun.com/billing
    private const val MODEL_QWEN_TURBO = "qwen-turbo"
    private const val MODEL_QWEN_PLUS = "qwen-plus"
    private const val MODEL_QWEN_MAX = "qwen-max"
    private const val MODEL_QWEN_MAX_LONG_CONTEXT = "qwen-max-longcontext"
    private const val MODEL_QWEN_1_8B_CHAT = "qwen-1.8b-chat"

    suspend fun syncSend(apiKey: String, model: String, temperature: Float, qwenQuestions: List<QwenQuestion>): BaseResult<String> {
        val request = buildChatRequest(apiKey, model, temperature, qwenQuestions)
        return try {
            val response = httpService.send(request)
            when (val result = parseChatResponse(response)) {
                is BaseResult.Success -> BaseResult.Success(result.desc, result.value.output.text)
                is BaseResult.Failure -> BaseResult.Failure(result.desc)
            }
        } catch (e: Exception) {
            BaseResult.Failure(e.message ?: e.toString())
        }
    }

    private fun buildChatRequest(apiKey: String, model: String, temperature: Float, qwenQuestions: List<QwenQuestion>): Request {
        val body = ChatRequestBody(
            model = model,
            input = ChatRequestBody.Input(qwenQuestions.check()),
            parameters = ChatRequestBody.Parameters("text", temperature)
        )
        return Request.Builder()
            .url("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
            .method("POST", GsonUtils.toJson(body).toRequestBody())
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", apiKey)
            .build()
    }

    private fun parseChatResponse(response: Response): BaseResult<ChatResponseBody> {
        val body = response.body?.string() ?: ""
        val jsonObject = GsonUtils.fromJson(body)
        return if (jsonObject["code"] != null) {
            BaseResult.Failure(jsonObject["message"].asString)
        } else if (jsonObject["output"] != null) {
            BaseResult.Success("请求成功", GsonUtils.fromJson(body, ChatResponseBody::class.java))
        } else {
            val error = "未知错误：code: ${response.code}, msg: ${response.message}"
            BaseResult.Failure(error)
        }
    }

    private fun List<QwenQuestion>.check(): List<QwenQuestion> {
        val newList = mutableListOf<QwenQuestion>()
        // 确保一问一答格式，并且最新的问题必须存在
        this.reversed().forEach { qwenQuestion ->
            if (newList.size and 1 == 0) {
                if (qwenQuestion.role == "user") {
                    newList.add(qwenQuestion)
                }
            } else {
                if (qwenQuestion.role == "assistant") {
                    newList.add(qwenQuestion)
                }
            }
        }
        // 上下文最多保留四次对话，不然免费tokens很快就用完了
        return if (newList.size > 9) newList.take(9).reversed() else newList.reversed()
    }
}