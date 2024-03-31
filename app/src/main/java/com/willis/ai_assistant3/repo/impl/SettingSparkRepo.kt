package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.UpdateSparkApiKey
import com.willis.ai_assistant3.data.bean.UpdateSparkApiSecret
import com.willis.ai_assistant3.data.bean.UpdateSparkAppId
import com.willis.ai_assistant3.data.bean.UpdateSparkTemperature
import com.willis.ai_assistant3.data.db.database.AppDatabase
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
    private val mAppIdFlow = MutableStateFlow("")
    override val appIdFlow: StateFlow<String> = mAppIdFlow

    private val mApiKeyFlow = MutableStateFlow("")
    override val apiKeyFlow: StateFlow<String> = mApiKeyFlow

    private val mApiSecretFlow = MutableStateFlow("")
    override val apiSecretFlow: StateFlow<String> = mApiSecretFlow

    private val mTemperatureFlow = MutableStateFlow(0F)
    override val temperatureFlow: StateFlow<Float> = mTemperatureFlow

    private val mPhone = appRepo.currentPhoneFlow.value!!
    private val mUserDetailDao = AppDatabase.instance.userDetailDao()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mUserDetailDao.queryByPhone(mPhone)?.let {
                mAppIdFlow.value = it.sparkAppId
                mApiKeyFlow.value = it.sparkApiKey
                mApiSecretFlow.value = it.sparkApiSecret
                mTemperatureFlow.value = it.sparkTemperature
            }
        }
    }

    override suspend fun updateAppId(newAppId: String): BaseResult<Unit> {
        val updateSparkAppId = UpdateSparkAppId(mPhone, newAppId)
        if (mUserDetailDao.updateSparkAppId(updateSparkAppId) == 0) return BaseResult.Failure("更新失败")
        mAppIdFlow.value = newAppId
        return BaseResult.Success(newAppId, Unit)
    }

    override suspend fun updateApiKey(newApiKey: String): BaseResult<Unit> {
        val updateSparkApiKey = UpdateSparkApiKey(mPhone, newApiKey)
        if (mUserDetailDao.updateSparkApiKey(updateSparkApiKey) == 0) return BaseResult.Failure("更新失败")
        mApiKeyFlow.value = newApiKey
        return BaseResult.Success(newApiKey, Unit)
    }

    override suspend fun updateApiSecret(newApiSecret: String): BaseResult<Unit> {
        val updateSparkApiSecret = UpdateSparkApiSecret(mPhone, newApiSecret)
        if (mUserDetailDao.updateSparkApiSecret(updateSparkApiSecret) == 0) return BaseResult.Failure("更新失败")
        mApiSecretFlow.value = newApiSecret
        return BaseResult.Success(newApiSecret, Unit)
    }

    override suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit> {
        val updateSparkTemperature = UpdateSparkTemperature(mPhone, newTemperature)
        mTemperatureFlow.value = newTemperature
        if (mUserDetailDao.updateSparkTemperature(updateSparkTemperature) == 0) return BaseResult.Failure("更新失败")
        return BaseResult.Success(newTemperature.toString(), Unit)
    }
}