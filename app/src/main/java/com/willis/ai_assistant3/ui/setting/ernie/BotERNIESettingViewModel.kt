package com.willis.ai_assistant3.ui.setting.ernie

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.repo.api.ISettingErnieRepo
import com.willis.ai_assistant3.repo.impl.SettingErnieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class BotERNIESettingViewModel : ViewModel() {
    private val mRepo: ISettingErnieRepo = SettingErnieRepo()

    val state = mRepo.state

    suspend fun updateClientId(newClientId: String) = withContext(Dispatchers.Default) {
        mRepo.updateClientId(newClientId)
    }

    suspend fun updateClientSecret(newClientSecret: String) = withContext(Dispatchers.Default) {
        mRepo.updateClientSecret(newClientSecret)
    }

    suspend fun refreshAccessToken() = withContext(Dispatchers.Default) {
        mRepo.refreshAccessToken()
    }

    suspend fun updateUrl(newUrl: String) = withContext(Dispatchers.Default) {
        mRepo.updateUrl(newUrl)
    }

    suspend fun updateTemperature(newTemperatureText: String) = withContext(Dispatchers.Default) {
        val default = 0.8F
        val newTemperature = try {
            val temp = newTemperatureText.toFloat()
            if (temp <= 0F || temp > 1) default else temp
        } catch (e: NumberFormatException) {
            default
        }
        mRepo.updateTemperature(newTemperature)
    }
}