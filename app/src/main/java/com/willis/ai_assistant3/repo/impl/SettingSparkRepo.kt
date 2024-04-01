package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.SettingSpark
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.api.ISettingSparkRepo
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
 * @date: 2023/12/15
 */
class SettingSparkRepo : ISettingSparkRepo {
    private val mState = MutableStateFlow<SettingSpark?>(null)
    override val state: StateFlow<SettingSpark?> = mState
    private val mPhone get() = appRepo.currentPhoneFlow.value!!

    init {
        CoroutineScope(Dispatchers.Default).launch {
            UserDatabase.getInstance(mPhone).settingSparkDao().queryByChatInfoId(-1)?.let {
                mState.value = it
            }
        }
    }

    override suspend fun updateAppId(newAppId: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(appId = newAppId))
    }

    override suspend fun updateApiKey(newApiKey: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(apiKey = newApiKey))
    }

    override suspend fun updateApiSecret(newApiSecret: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(apiSecret = newApiSecret))
    }

    override suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(temperature = newTemperature))
    }

    private suspend fun realUpdate(newState: SettingSpark?): BaseResult<Unit> {
        newState?.let {
            mState.value = newState
            UserDatabase.getInstance(mPhone).settingSparkDao().insertOrReplace(newState)
            return BaseResult.Success("更新成功", Unit)
        }
        return BaseResult.Failure("更新失败")
    }
}