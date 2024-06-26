package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.data.bean.SettingErnie
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.api.ISettingErnieRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import com.willis.base.services.toastService
import com.willis.base.utils.AppUtils
import com.willis.base.utils.NetworkUtils
import com.willis.ernie.Ernie
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
class SettingErnieRepo(private val chatInfoId: Long) : ISettingErnieRepo {
    private val mState = MutableStateFlow<SettingErnie?>(null)
    override val state: StateFlow<SettingErnie?> = mState

    private val mPhone get() = appRepo.currentPhoneFlow.value!!

    init {
        CoroutineScope(Dispatchers.Default).launch {
            UserDatabase.getInstance(mPhone).settingErnieDao().queryByChatInfoId(chatInfoId)?.let {
                mState.value = it
            }
        }
    }

    override suspend fun updateClientId(newClientId: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(clientId = newClientId))
    }

    override suspend fun updateClientSecret(newClientSecret: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(clientSecret = newClientSecret))
    }

    override suspend fun refreshAccessToken(): BaseResult<Unit> {
        if (!NetworkUtils.isNetworkUsable(AppUtils.appContext)) {
            return BaseResult.Failure(AppUtils.getString(R.string.app_network_unusable))
        }
        mState.value?.let {
            return when (val refreshResult = Ernie.refreshAccessToken(it.clientId, it.clientSecret)) {
                is BaseResult.Success -> updateAccessToken(refreshResult.value)
                is BaseResult.Failure -> BaseResult.Failure(refreshResult.desc)
            }
        }
        return BaseResult.Failure("mState == null")
    }

    override suspend fun updateUrl(newUrl: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(url = newUrl))
    }

    override suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(temperature = newTemperature))
    }

    override suspend fun updateContextTimes(newContextTimes: Int): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(contextTimes = newContextTimes))
    }

    private suspend fun updateAccessToken(newAccessToken: String): BaseResult<Unit> {
        return realUpdate(mState.value?.copy(accessToken = newAccessToken))
    }

    private suspend fun realUpdate(newState: SettingErnie?): BaseResult<Unit> {
        newState?.let {
            mState.value = newState
            UserDatabase.getInstance(mPhone).settingErnieDao().insertOrReplace(newState)
            return BaseResult.Success("更新成功", Unit)
        }
        return BaseResult.Failure("更新失败")
    }
}