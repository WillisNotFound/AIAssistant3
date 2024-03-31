package com.willis.ernie

import com.willis.base.data.BaseResult
import com.willis.base.services.httpService
import com.willis.base.utils.GsonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


/**
 * description: 文心一言大模型
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
object Ernie {
    // 可用服务：https://console.bce.baidu.com/qianfan/ais/console/onlineService
    private const val URL_ACCESS_TOKEN = "https://aip.baidubce.com/oauth/2.0/token"
    private const val URL_LLAMA_2_13B =
        "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/qianfan_chinese_llama_2_13b"
    private const val URL_YI_34B_CHAT =
        "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/yi_34b_chat"

    suspend fun refreshAccessToken(clientId: String, clientSecret: String): BaseResult<String> {
        val request = buildAccessTokenRequest(clientId, clientSecret)
        val response = httpService.send(request)
        return when (val accessToken = parseAccessTokenResponse(response)) {
            is AccessToken.Success -> BaseResult.Success("刷新成功", accessToken.accessToken)
            is AccessToken.Failure -> BaseResult.Failure(accessToken.errorDescription)
        }
    }

    suspend fun syncSend(
        accessToken: String,
        url: String,
        temperature: Float,
        ernieQuestions: List<ErnieQuestion>
    ): BaseResult<String> {
        val request = buildChatRequest(accessToken, url, temperature, ernieQuestions)
        return try {
            val response = httpService.send(request)
            when (val result = parseChatResponse(response)) {
                is BaseResult.Success -> BaseResult.Success(result.desc, result.value.result)
                is BaseResult.Failure -> BaseResult.Failure(result.desc)
            }
        } catch (e: Exception) {
            BaseResult.Failure(e.message ?: e.toString())
        }
    }

    private fun buildAccessTokenRequest(clientId: String, clientSecret: String) = Request.Builder()
        .url("$URL_ACCESS_TOKEN?client_id=$clientId&client_secret=$clientSecret&grant_type=client_credentials")
        .method("POST", "".toRequestBody("application/json".toMediaTypeOrNull()))
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json")
        .build()

    private fun parseAccessTokenResponse(response: Response): AccessToken {
        val body = response.body?.string() ?: ""
        val jsonObject = GsonUtils.fromJson(body)
        return if (jsonObject["error"] != null) {
            GsonUtils.fromJson(body, AccessToken.Failure::class.java)
        } else if (jsonObject["access_token"] != null) {
            GsonUtils.fromJson(body, AccessToken.Success::class.java)
        } else {
            val error = "未知错误：code: ${response.code}, msg: ${response.message}"
            AccessToken.Failure(error, error)
        }
    }

    private fun buildChatRequest(
        accessToken: String,
        url: String,
        temperature: Float,
        ernieQuestions: List<ErnieQuestion>
    ): Request {
        val body = ChatRequestBody(ernieQuestions.check(), temperature)
        return Request.Builder()
            .url("$url?access_token=$accessToken")
            .method("POST", GsonUtils.toJson(body).toRequestBody())
            .addHeader("Content-Type", "application/json")
            .build()
    }

    private fun parseChatResponse(response: Response): BaseResult<ChatResponseBody> {
        val body = response.body?.string() ?: ""
        val jsonObject = GsonUtils.fromJson(body)
        return if (jsonObject["error_msg"] != null) {
            BaseResult.Failure(jsonObject["error_msg"].asString)
        } else if (jsonObject["id"] != null) {
            BaseResult.Success("请求成功", GsonUtils.fromJson(body, ChatResponseBody::class.java))
        } else {
            val error = "未知错误：code: ${response.code}, msg: ${response.message}"
            BaseResult.Failure(error)
        }
    }

    private fun List<ErnieQuestion>.check(): List<ErnieQuestion> {
        val newList = mutableListOf<ErnieQuestion>()
        // 确保一问一答格式，并且最新的问题必须存在
        this.reversed().forEach { ernieQuestion ->
            if (newList.size and 1 == 0) {
                if (ernieQuestion.role == "user") {
                    newList.add(ernieQuestion)
                }
            } else {
                if (ernieQuestion.role == "assistant") {
                    newList.add(ernieQuestion)
                }
            }
        }
        // 上下文最多保留四次对话，不然免费tokens很快就用完了
        return if (newList.size > 9) newList.take(9).reversed() else newList.reversed()
    }
}