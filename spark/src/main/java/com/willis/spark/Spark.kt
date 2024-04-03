package com.willis.spark

import android.content.Context
import android.util.Log
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMConfig
import com.iflytek.sparkchain.core.SparkChain
import com.iflytek.sparkchain.core.SparkChainConfig
import com.willis.base.data.BaseResult
import com.willis.base.services.toastService
import com.willis.base.utils.GsonUtils

/**
 * description: [讯飞星火大模型](https://www.xfyun.cn/doc/spark/AndroidSDK.htm)
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/27
 */
object Spark {
    private const val TAG = "Spark"
    private var llm: LLM? = null

    fun initSDK(context: Context, appID: String, apiKey: String, apiSecret: String, temperature: Float) {
        val sparkChainConfig = SparkChainConfig.builder()
            .appID(appID)
            .apiKey(apiKey)
            .apiSecret(apiSecret)
            .logLevel(0)
        when (val resultCode = SparkChain.getInst().init(context.applicationContext, sparkChainConfig)) {
            0 -> {
                toastService.showRight("Spark SDK 初始化成功")
                initLLM(temperature)
            }

            else -> {
                toastService.showError("Spark SDK 初始化失败, ${Error.map[resultCode]}")
            }
        }
    }

    fun unInitSDK() {
        SparkChain.getInst().unInit()
        llm = null
    }

    /**
     * 同步调用
     */
    suspend fun syncSend(sparkMessages: List<SparkMessage>): BaseResult<String> {
        llm?.let {
            val syncOutput = it.run(GsonUtils.toJson(sparkMessages.check()))
            if (syncOutput.errCode == 0) {
                Log.d(
                    TAG, "content = ${syncOutput.content}, " +
                            "completionTokens = ${syncOutput.completionTokens}, " +
                            "promptTokens = ${syncOutput.promptTokens}, " +
                            "totalTokens = ${syncOutput.totalTokens}"
                )
                return BaseResult.Success("同步调用成功", syncOutput.content)
            } else {
                return BaseResult.Failure(syncOutput.errMsg)
            }
        }
        return BaseResult.Failure("LLM is null")
    }

    private fun initLLM(temperature: Float) {
        val llmConfig = LLMConfig.builder()
            .domain("generalv3")
            .maxToken(2048)
            .temperature(temperature)
            .topK(4)
        llm = LLM(llmConfig)
    }

    private fun List<SparkMessage>.check(): List<SparkMessage> {
        val newList = mutableListOf<SparkMessage>()
        // 确保一问一答格式，并且最新的问题必须存在
        for (i in this.size - 1 downTo 0) {
            val sparkQuestion = this[i]
            if (newList.size and 1 == 0) {
                if (sparkQuestion.role == "user") {
                    newList.add(sparkQuestion)
                }
            } else {
                if (sparkQuestion.role == "assistant") {
                    newList.add(sparkQuestion)
                }
            }
            // 上下文最多保留四次对话
            if (newList.size == 9) break
        }
        return newList.reversed()
    }
}