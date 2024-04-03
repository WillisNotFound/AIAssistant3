package com.willis.ai_assistant3.ui.setting.spark

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.repo.api.ISettingSparkRepo
import com.willis.ai_assistant3.repo.impl.SettingSparkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class BotSparkSettingViewModel(chatInfoId: Long) : ViewModel() {
    private val mRepo: ISettingSparkRepo = SettingSparkRepo(chatInfoId)

    val state = mRepo.state

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

    suspend fun updateContextTimes(newContextTimesText: String) = withContext(Dispatchers.Default) {
        val default = 4
        val newContextTimes = try {
            val temp = newContextTimesText.toInt()
            if (temp < 0) default else temp
        } catch (e: NumberFormatException) {
            default
        }
        mRepo.updateContextTimes(newContextTimes)
    }
}