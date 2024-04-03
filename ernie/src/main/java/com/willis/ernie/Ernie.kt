package com.willis.ernie

import com.willis.base.data.BaseResult
import com.willis.base.services.httpService
import com.willis.base.utils.GsonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


/**
 * description: 文心一言大模型, https://console.bce.baidu.com/qianfan/ais/console/onlineService
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
object Ernie {
    private const val URL_ACCESS_TOKEN = "https://aip.baidubce.com/oauth/2.0/token"
    private const val URL_LLAMA_2_13B = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/qianfan_chinese_llama_2_13b"
    private const val URL_YI_34B_CHAT = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/yi_34b_chat"

    suspend fun refreshAccessToken(clientId: String, clientSecret: String): BaseResult<String> {
        val request = buildAccessTokenRequest(clientId, clientSecret)
        val response = httpService.send(request)
        return when (val accessToken = parseAccessTokenResponse(response)) {
            is AccessToken.Success -> BaseResult.Success("刷新成功", accessToken.accessToken)
            is AccessToken.Failure -> BaseResult.Failure(accessToken.errorDescription)
        }
    }

    suspend fun sendMessage(
        accessToken: String,
        modelUrl: String,
        temperature: Float,
        messages: List<ErnieMessage>
    ): BaseResult<String> {
        val request = buildSendMessageRequest(accessToken, modelUrl, temperature, messages)
        return try {
            val response = httpService.send(request)
            when (val result = parseSendMessageResponse(response)) {
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

    private fun buildSendMessageRequest(
        accessToken: String,
        modelUrl: String,
        temperature: Float,
        messages: List<ErnieMessage>
    ) = Request.Builder()
        .url("$modelUrl?access_token=$accessToken")
        .method("POST", GsonUtils.toJson(RequestBody(messages, temperature)).toRequestBody())
        .addHeader("Content-Type", "application/json")
        .build()

    private fun parseSendMessageResponse(response: Response): BaseResult<ResponseBody> {
        val body = response.body?.string() ?: ""
        val jsonObject = GsonUtils.fromJson(body)
        return if (jsonObject["error_msg"] != null) {
            BaseResult.Failure(jsonObject["error_msg"].asString)
        } else if (jsonObject["id"] != null) {
            BaseResult.Success("请求成功", GsonUtils.fromJson(body, ResponseBody::class.java))
        } else {
            val error = "未知错误：code: ${response.code}, msg: ${response.message}"
            BaseResult.Failure(error)
        }
    }
}