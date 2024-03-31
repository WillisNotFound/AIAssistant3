package com.willis.ai_assistant3.repo.api

import com.willis.base.data.BaseResult
import kotlinx.coroutines.flow.StateFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/14
 */
interface IAppRepo {
    val currentPhoneFlow: StateFlow<String?>

    /**
     * 是否已登录
     */
    suspend fun isLogin(): Boolean

    /**
     * 登录
     */
    suspend fun login(phone: String): BaseResult<Unit>

    /**
     * 管理员登录登录
     */
    suspend fun adminLogin(): BaseResult<Unit>

    /**
     * 退出登录
     */
    suspend fun logoff(): BaseResult<Unit>

    /**
     * 切换用户
     */
    suspend fun switchAccount(phone: String): BaseResult<Unit>
}