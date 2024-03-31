package com.willis.ai_assistant3.repo.api

import com.willis.base.data.BaseResult
import kotlinx.coroutines.flow.StateFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2024/1/1
 */
interface ISettingAccountRepo {
    val phoneFlow: StateFlow<String>

    val nicknameFlow: StateFlow<String>

    val createTimeFlow: StateFlow<String>

    suspend fun updateNickname(newNickname: String): BaseResult<Unit>
}