package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.UserDetail
import com.willis.ai_assistant3.data.bean.UserInfo
import com.willis.ai_assistant3.data.db.database.AppDatabase
import com.willis.ai_assistant3.repo.api.IAppRepo
import com.willis.base.data.BaseResult
import com.willis.base.services.kvService
import com.willis.base.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/14
 */
object AppRepo : IAppRepo {
    private const val KEY_CURRENT_USER_PHONE = "current_user_phone"

    private const val DEFAULT_ERNIE_CLIENT_ID = "hoMebgV43sMUTgi1MncITDP3"
    private const val DEFAULT_ERNIE_CLIENT_SECRET = "dbI8mSEDoH1ppqoVtGqGdSLUVy7812K3"
    private const val DEFAULT_ERNIE_ACCESS_TOKEN =
        "24.d61858adb5a3ae3433e4c7ac0d51bec3.2592000.1705567522.282335-41652448"
    private const val DEFAULT_ERNIE_URL =
        "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/yi_34b_chat"
    private const val DEFAULT_ERNIE_TEMPERATURE = 0.8F
    private const val DEFAULT_SPARK_APP_ID = "81382459"
    private const val DEFAULT_SPARK_API_KEY = "5450770cd5813955d06d2625ccc38422"
    private const val DEFAULT_SPARK_API_SECRET = "MGNiMjdkODQwZDFlNjRkNzQzYjY3MmVl"
    private const val DEFAULT_SPARK_TEMPERATURE = 0.5F
    private const val DEFAULT_QWEN_API_KEY = "sk-e1f1f8d875974f26af60e1fec025bd59"
    private const val DEFAULT_QWEN_MODEL = "qwen-1.8b-chat"
    private const val DEFAULT_QWEN_TEMPERATURE = 0.85F

    private val mCurrentPhoneFlow = MutableStateFlow<String?>(null)
    override val currentPhoneFlow: StateFlow<String?> = mCurrentPhoneFlow

    private val mUserInfoDao get() = AppDatabase.instance.userInfoDao()

    override suspend fun isLogin(): Boolean {
        val loggedUserPhone = getLoggedUserPhone() ?: return false
        val userInfo = mUserInfoDao.queryByPhone(loggedUserPhone) ?: return false
        mUserInfoDao.update(userInfo.updateLoginMillis())
        mCurrentPhoneFlow.value = userInfo.phone
        return true
    }

    override suspend fun login(phone: String): BaseResult<Unit> {
        val userInfo = mUserInfoDao.queryByPhone(phone) ?: autoRegister(phone, false)
        updateLoggedUserPhone(userInfo.phone)
        mUserInfoDao.insertOrReplace(userInfo.updateLoginMillis())
        mCurrentPhoneFlow.value = userInfo.phone
        return BaseResult.Success("欢迎: ${userInfo.nickname}", Unit)
    }

    override suspend fun adminLogin(): BaseResult<Unit> {
        val userInfo = mUserInfoDao.queryByPhone("admin") ?: autoRegister("admin", true)
        updateLoggedUserPhone(userInfo.phone)
        mUserInfoDao.insertOrReplace(userInfo.updateLoginMillis())
        mCurrentPhoneFlow.value = userInfo.phone
        return BaseResult.Success("欢迎: ${userInfo.nickname}", Unit)
    }

    override suspend fun logoff(): BaseResult<Unit> {
        updateLoggedUserPhone(null)
        mCurrentPhoneFlow.value = null
        return BaseResult.Success("退出登录", Unit)
    }

    override suspend fun switchAccount(phone: String): BaseResult<Unit> {
        val logoffResult = logoff()
        if (logoffResult is BaseResult.Failure) return logoffResult
        return login(phone)
    }

    /**
     * 自动注册
     */
    private suspend fun autoRegister(phone: String, isAdmin: Boolean): UserInfo {
        val userInfo = UserInfo(
            phone = phone,
            nickname = "user-$phone",
            createMillis = DateUtils.currentMillis(),
            lastLoginMillis = DateUtils.currentMillis()
        )
        val userDetail = UserDetail(
            phone = phone,
            ernieClientId = if (isAdmin) DEFAULT_ERNIE_CLIENT_ID else DEFAULT_ERNIE_CLIENT_ID,
            ernieClientSecret = if (isAdmin) DEFAULT_ERNIE_CLIENT_SECRET else DEFAULT_ERNIE_CLIENT_SECRET,
            ernieAccessToken = if (isAdmin) DEFAULT_ERNIE_ACCESS_TOKEN else DEFAULT_ERNIE_ACCESS_TOKEN,
            ernieUrl = if (isAdmin) DEFAULT_ERNIE_URL else DEFAULT_ERNIE_URL,
            ernieTemperature = DEFAULT_ERNIE_TEMPERATURE,
            sparkAppId = if (isAdmin) DEFAULT_SPARK_APP_ID else "",
            sparkApiKey = if (isAdmin) DEFAULT_SPARK_API_KEY else "",
            sparkApiSecret = if (isAdmin) DEFAULT_SPARK_API_SECRET else "",
            sparkTemperature = DEFAULT_SPARK_TEMPERATURE,
            qwenApiKey = if (isAdmin) DEFAULT_QWEN_API_KEY else "",
            qwenModel = if (isAdmin) DEFAULT_QWEN_MODEL else "",
            qwenTemperature = DEFAULT_QWEN_TEMPERATURE
        )
        mUserInfoDao.insertOrReplace(userInfo)
        AppDatabase.instance.userDetailDao().insertOrReplace(userDetail)
        return userInfo
    }

    /**
     * 获取已在该设备登录的用户手机号
     */
    private fun getLoggedUserPhone(): String? {
        return kvService.getString(KEY_CURRENT_USER_PHONE, null)
    }

    /**
     * 更新已在该设备登录的用户手机号
     */
    private fun updateLoggedUserPhone(phone: String?) {
        kvService.putString(KEY_CURRENT_USER_PHONE, phone)
    }

    private fun UserInfo.updateLoginMillis() = UserInfo(
        phone = phone,
        nickname = nickname,
        createMillis = createMillis,
        lastLoginMillis = DateUtils.currentMillis()
    )
}