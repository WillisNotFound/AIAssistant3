package com.willis.ai_assistant3.ui.setting.qwen

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.repo.api.ISettingQwenRepo
import com.willis.ai_assistant3.repo.impl.SettingQwenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class BotQwenSettingViewModel(chatInfoId: Long) : ViewModel() {
    private val mRepo: ISettingQwenRepo = SettingQwenRepo(chatInfoId)

    val state = mRepo.state

    suspend fun updateApiKey(newApiKey: String) = withContext(Dispatchers.Default) {
        mRepo.updateApiKey(newApiKey)
    }

    suspend fun updateModel(newModel: String) = withContext(Dispatchers.Default) {
        mRepo.updateModel(newModel)
    }

    suspend fun updateTemperature(newTemperatureText: String) = withContext(Dispatchers.Default) {
        val default = 0.85F
        val newTemperature = try {
            val temp = newTemperatureText.toFloat()
            if (temp <= 0F || temp >= 2) default else temp
        } catch (e: NumberFormatException) {
            default
        }
        mRepo.updateTemperature(newTemperature)
    }
}