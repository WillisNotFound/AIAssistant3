package com.willis.ai_assistant3.ui.setting.account

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.repo.api.ISettingAccountRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.ai_assistant3.repo.impl.SettingAccountRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/29
 */
class AccountSettingViewModel : ViewModel() {
    private val mRepo: ISettingAccountRepo = SettingAccountRepo()

    val phoneFlow: StateFlow<String> = mRepo.phoneFlow

    val nicknameFlow: StateFlow<String> = mRepo.nicknameFlow

    val createTimeFlow: StateFlow<String> = mRepo.createTimeFlow

    suspend fun updateNickname(newNickname: String) = withContext(Dispatchers.Default) {
        mRepo.updateNickname(newNickname)
    }

    suspend fun switchAccount(phone: String) = withContext(Dispatchers.Default) {
        appRepo.switchAccount(phone)
    }

    suspend fun exit() = withContext(Dispatchers.Default) {
        appRepo.logoff()
    }
}