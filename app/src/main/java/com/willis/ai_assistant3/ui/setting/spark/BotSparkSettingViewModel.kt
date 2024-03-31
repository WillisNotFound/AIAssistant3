package com.willis.ai_assistant3.ui.setting.spark

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.repo.api.ISettingSparkRepo
import com.willis.ai_assistant3.repo.impl.SettingSparkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class BotSparkSettingViewModel : ViewModel() {
    private val mRepo: ISettingSparkRepo = SettingSparkRepo()

    val appIdFlow: StateFlow<String> = mRepo.appIdFlow
    val apiKeyFlow: StateFlow<String> = mRepo.apiKeyFlow
    val apiSecretFlow: StateFlow<String> = mRepo.apiSecretFlow
    val temperatureFlow: StateFlow<Float> = mRepo.temperatureFlow

    suspend fun updateAppId(newAppId: String) = withContext(Dispatchers.Default) {
        mRepo.updateAppId(newAppId)
    }

    suspend fun updateApiKey(newApiKey: String) = withContext(Dispatchers.Default) {
        mRepo.updateApiKey(newApiKey)
    }

    suspend fun updateApiSecret(newApiSecret: String) = withContext(Dispatchers.Default) {
        mRepo.updateApiSecret(newApiSecret)
    }

    suspend fun updateTemperature(newTemperatureText: String) = withContext(Dispatchers.Default) {
        val default = 0.5F
        val newTemperature = try {
            val temp = newTemperatureText.toFloat()
            if (temp <= 0F || temp > 1) default else temp
        } catch (e: NumberFormatException) {
            default
        }
        mRepo.updateTemperature(newTemperature)
    }
}