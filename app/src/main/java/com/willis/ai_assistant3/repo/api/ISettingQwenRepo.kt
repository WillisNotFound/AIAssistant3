package com.willis.ai_assistant3.repo.api

import com.willis.base.data.BaseResult
import kotlinx.coroutines.flow.StateFlow

/**
 * description: 阿里千问大模型配置
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
interface ISettingQwenRepo {
    val apiKeyFlow: StateFlow<String>

    val modelFlow: StateFlow<String>

    val temperatureFlow: StateFlow<Float>

    suspend fun updateApiKey(newApiKey: String): BaseResult<Unit>

    suspend fun updateModel(newModel: String): BaseResult<Unit>

    suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit>
}