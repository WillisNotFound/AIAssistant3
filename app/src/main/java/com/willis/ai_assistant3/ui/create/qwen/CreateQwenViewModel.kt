package com.willis.ai_assistant3.ui.create.qwen

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.SettingQwen
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.appRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateQwenViewModel : ViewModel() {
    private val mPhone get() = appRepo.currentPhoneFlow.value!!

    suspend fun getDefaultSetting(): SettingQwen? {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).settingQwenDao().queryByChatInfoId(-1)
        }
    }

    suspend fun createChat(chatInfo: ChatInfo): Long {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).chatInfoDao().insert(chatInfo)
        }
    }

    suspend fun createSetting(settingQwen: SettingQwen) {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).settingQwenDao().insertOrReplace(settingQwen)
        }
    }
}