package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.UpdateErnieAccessToken
import com.willis.ai_assistant3.data.bean.UpdateErnieClientId
import com.willis.ai_assistant3.data.bean.UpdateErnieClientSecret
import com.willis.ai_assistant3.data.bean.UpdateErnieTemperature
import com.willis.ai_assistant3.data.bean.UpdateErnieUrl
import com.willis.ai_assistant3.data.db.database.AppDatabase
import com.willis.ai_assistant3.repo.api.ISettingErnieRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
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
class SettingErnieRepo : ISettingErnieRepo {
    private val mClientIdFlow = MutableStateFlow("")
    override val clientIdFlow: StateFlow<String> = mClientIdFlow

    private val mClientSecretFlow = MutableStateFlow("")
    override val clientSecretFlow: StateFlow<String> = mClientSecretFlow

    private val mAccessTokenFlow = MutableStateFlow("")
    override val accessTokenFlow: StateFlow<String> = mAccessTokenFlow

    private val mUrlFlow = MutableStateFlow("")
    override val urlFlow: StateFlow<String> = mUrlFlow

    private val mTemperatureFlow = MutableStateFlow(0F)
    override val temperatureFlow: StateFlow<Float> = mTemperatureFlow

    private val mPhone = appRepo.currentPhoneFlow.value!!
    private val mUserDetailDao = AppDatabase.instance.userDetailDao()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mUserDetailDao.queryByPhone(mPhone)?.let {
                mClientIdFlow.value = it.ernieClientId
                mClientSecretFlow.value = it.ernieClientSecret
                mAccessTokenFlow.value = it.ernieAccessToken
                mUrlFlow.value = it.ernieUrl
                mTemperatureFlow.value = it.ernieTemperature
            }
        }
    }

    override suspend fun updateClientId(newClientId: String): BaseResult<Unit> {
        val updateErnieClientId = UpdateErnieClientId(mPhone, newClientId)
        if (mUserDetailDao.updateErnieClientId(updateErnieClientId) == 0) return BaseResult.Failure("更新失败")
        mClientIdFlow.value = newClientId
        return BaseResult.Success(newClientId, Unit)
    }

    override suspend fun updateClientSecret(newClientSecret: String): BaseResult<Unit> {
        val updateErnieClientSecret = UpdateErnieClientSecret(mPhone, newClientSecret)
        mClientSecretFlow.value = newClientSecret
        if (mUserDetailDao.updateErnieClientSecret(updateErnieClientSecret) == 0) return BaseResult.Failure("更新失败")
        return BaseResult.Success(newClientSecret, Unit)
    }

    override suspend fun refreshAccessToken(): BaseResult<Unit> {
        return when (val refreshResult = Ernie.refreshAccessToken(mClientIdFlow.value, mClientSecretFlow.value)) {
            is BaseResult.Success -> updateAccessToken(refreshResult.value)
            is BaseResult.Failure -> BaseResult.Failure(refreshResult.desc)
        }
    }

    override suspend fun updateUrl(newUrl: String): BaseResult<Unit> {
        val updateErnieUrl = UpdateErnieUrl(mPhone, newUrl)
        mUrlFlow.value = newUrl
        if (mUserDetailDao.updateErnieUrl(updateErnieUrl) == 0) return BaseResult.Failure("更新失败")
        return BaseResult.Success(newUrl, Unit)
    }

    override suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit> {
        val updateErnieTemperature = UpdateErnieTemperature(mPhone, newTemperature)
        mTemperatureFlow.value = newTemperature
        if (mUserDetailDao.updateErnieTemperature(updateErnieTemperature) == 0) return BaseResult.Failure("更新失败")
        return BaseResult.Success(newTemperature.toString(), Unit)
    }

    private suspend fun updateAccessToken(newAccessToken: String): BaseResult<Unit> {
        val updateErnieAccessToken = UpdateErnieAccessToken(mPhone, newAccessToken)
        if (mUserDetailDao.updateErnieAccessToken(updateErnieAccessToken) == 0) return BaseResult.Failure("更新失败")
        mAccessTokenFlow.value = newAccessToken
        return BaseResult.Success(newAccessToken, Unit)
    }
}