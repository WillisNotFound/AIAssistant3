package com.willis.ai_assistant3.ui.create.ernie

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.SettingErnie
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.appRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateErnieViewModel : ViewModel() {
    private val mPhone get() = appRepo.currentPhoneFlow.value!!

    suspend fun getDefaultSetting(): SettingErnie? {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).settingErnieDao().queryByChatInfoId(-1)
        }
    }

    suspend fun createChat(chatInfo: ChatInfo): Long {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).chatInfoDao().insert(chatInfo)
        }
    }

    suspend fun createSetting(settingErnie: SettingErnie) {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).settingErnieDao().insertOrReplace(settingErnie)
        }
    }
}