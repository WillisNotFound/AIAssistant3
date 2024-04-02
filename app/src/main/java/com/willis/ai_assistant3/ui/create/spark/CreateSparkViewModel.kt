package com.willis.ai_assistant3.ui.create.spark

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.SettingSpark
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.appRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateSparkViewModel : ViewModel() {
    private val mPhone get() = appRepo.currentPhoneFlow.value!!

    suspend fun getDefaultSetting(): SettingSpark? {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).settingSparkDao().queryByChatInfoId(-1)
        }
    }

    suspend fun createChat(chatInfo: ChatInfo): Long {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).chatInfoDao().insert(chatInfo)
        }
    }

    suspend fun createSetting(settingSpark: SettingSpark) {
        return withContext(Dispatchers.Default) {
            UserDatabase.getInstance(mPhone).settingSparkDao().insertOrReplace(settingSpark)
        }
    }
}