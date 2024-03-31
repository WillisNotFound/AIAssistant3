package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.UpdateQwenApiKey
import com.willis.ai_assistant3.data.bean.UpdateQwenModel
import com.willis.ai_assistant3.data.bean.UpdateQwenTemperature
import com.willis.ai_assistant3.data.db.database.AppDatabase
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
class SettingQwenRepo : ISettingQwenRepo {
    private val mApiKeyFlow = MutableStateFlow("")
    override val apiKeyFlow: StateFlow<String> = mApiKeyFlow

    private val mModelFlow = MutableStateFlow("")
    override val modelFlow: StateFlow<String> = mModelFlow

    private val mTemperatureFlow = MutableStateFlow(0F)
    override val temperatureFlow: StateFlow<Float> = mTemperatureFlow

    private val mPhone = appRepo.currentPhoneFlow.value!!
    private val mUserDetailDao = AppDatabase.instance.userDetailDao()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mUserDetailDao.queryByPhone(mPhone)?.let {
                mApiKeyFlow.value = it.qwenApiKey
                mModelFlow.value = it.qwenModel
                mTemperatureFlow.value = it.qwenTemperature
            }
        }
    }

    override suspend fun updateApiKey(newApiKey: String): BaseResult<Unit> {
        val updateQwenApiKey = UpdateQwenApiKey(mPhone, newApiKey)
        if (mUserDetailDao.updateQwenApiKey(updateQwenApiKey) == 0) return BaseResult.Failure("更新失败")
        mApiKeyFlow.value = newApiKey
        return BaseResult.Success(newApiKey, Unit)
    }

    override suspend fun updateModel(newModel: String): BaseResult<Unit> {
        val updateQwenModel = UpdateQwenModel(mPhone, newModel)
        if (mUserDetailDao.updateQwenModel(updateQwenModel) == 0) return BaseResult.Failure("更新失败")
        mModelFlow.value = newModel
        return BaseResult.Success(newModel, Unit)
    }

    override suspend fun updateTemperature(newTemperature: Float): BaseResult<Unit> {
        val updateQwenTemperature = UpdateQwenTemperature(mPhone, newTemperature)
        mTemperatureFlow.value = newTemperature
        if (mUserDetailDao.updateQwenTemperature(updateQwenTemperature) == 0) return BaseResult.Failure("更新失败")
        return BaseResult.Success(newTemperature.toString(), Unit)
    }
}