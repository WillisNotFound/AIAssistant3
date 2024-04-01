package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.UpdateNickname
import com.willis.ai_assistant3.data.db.database.AppDatabase
import com.willis.ai_assistant3.repo.api.ISettingAccountRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import com.willis.base.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2024/1/1
 */
class SettingAccountRepo : ISettingAccountRepo {
    private val mPhoneFlow = MutableStateFlow("")
    override val phoneFlow: StateFlow<String> = mPhoneFlow

    private val mNicknameFlow = MutableStateFlow("")
    override val nicknameFlow: StateFlow<String> = mNicknameFlow

    private val mCreateTimeFlow = MutableStateFlow("")
    override val createTimeFlow: StateFlow<String> = mCreateTimeFlow

    private val mPhone = appRepo.currentPhoneFlow.value!!
    private val mUserInfoDao = AppDatabase.instance.userInfoDao()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mUserInfoDao.queryByPhone(mPhone)?.let {
                mPhoneFlow.value = it.phone
                mNicknameFlow.value = it.nickname
                mCreateTimeFlow.value = DateUtils.getFormatTime(it.createMillis)
            }
        }
    }

    override suspend fun updateNickname(newNickname: String): BaseResult<Unit> {
        val updateNickname = UpdateNickname(mPhone, newNickname)
        if (mUserInfoDao.updateNickname(updateNickname) == 0) return BaseResult.Failure("更新失败")
        mNicknameFlow.value = newNickname
        return BaseResult.Success(newNickname, Unit)
    }
}