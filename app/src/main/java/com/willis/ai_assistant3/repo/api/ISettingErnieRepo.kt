package com.willis.ai_assistant3.repo.api

import com.willis.base.data.BaseResult
import kotlinx.coroutines.flow.StateFlow

/**
 * description: 文心一言大模型配置
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
interface ISettingErnieRepo {
    val clientIdFlow: StateFlow<String>

    val clientSecretFlow: StateFlow<String>

    val accessTokenFlow: StateFlow<String>

    val urlFlow: StateFlow<String>

    val temperatureFlow: StateFlow<Float>

    suspend fun updateClientId(newClientId: String): BaseResult<Unit>

    suspend fun updateClientSecret(newClientSecret: String): BaseResult<Unit>

    suspend fun refreshAccessToken(): BaseResult<Unit>

    suspend fun updateUrl(newUrl: String): BaseResult<Unit>

    suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit>
}