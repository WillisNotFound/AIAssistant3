package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.SettingQwen
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.api.ISettingQwenRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class SettingQwenRepo(chatInfoId: Long) : ISettingQwenRepo {
    private val mState = MutableStateFlow<SettingQwen?>(null)
    override val state: StateFlow<SettingQwen?> = mState

    private val mPhone get() = appRepo.currentPhoneFlow.value!!

    init {
        CoroutineScope(Dispatchers.Default).launch {
            UserDatabase.getInstance(mPhone).settingQwenDao().queryByChatInfoId(chatInfoId)?.let {
                mState.value = it
            }
        }
    }

    override suspend fun updateApiKey(newApiKey: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(apiKey = newApiKey))
    }

    override suspend fun updateModel(newModel: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(model = newModel))
    }

    override suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(temperature = newTemperature))
    }

    override suspend fun updateEnableSearch(newEnableSearch: Boolean): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(enableSearch = newEnableSearch))
    }

    override suspend fun updateContextTimes(newContextTimes: Int): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(contextTimes = newContextTimes))
    }

    private suspend fun realUpdate(newState: SettingQwen?): BaseResult<Unit> {
        newState?.let {
            mState.value = newState
            UserDatabase.getInstance(mPhone).settingQwenDao().insertOrReplace(newState)
            return BaseResult.Success("更新成功", Unit)
        }
        return BaseResult.Failure("更新失败")
    }
}