package com.willis.ai_assistant3.repo.api

import com.willis.base.data.BaseResult
import kotlinx.coroutines.flow.StateFlow

/**
 * description: 讯飞星火大模型配置
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
interface ISettingSparkRepo {
    val appIdFlow: StateFlow<String>

    val apiKeyFlow: StateFlow<String>

    val apiSecretFlow: StateFlow<String>

    val temperatureFlow: StateFlow<Float>

    suspend fun updateAppId(newAppId: String): BaseResult<Unit>

    suspend fun updateApiKey(newApiKey: String): BaseResult<Unit>

    suspend fun updateApiSecret(newApiSecret: String): BaseResult<Unit>

    suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit>
}