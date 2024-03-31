package com.willis.ai_assistant3.repo.api

import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.base.data.BaseResult
import kotlinx.coroutines.flow.SharedFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
interface IChatInfoRepo {
    val chatInfoListFlow: SharedFlow<List<ChatInfo>>

    suspend fun addChatInfo(chatInfo: ChatInfo): BaseResult<Unit>

    suspend fun deleteChatInfo(chatInfoId: Long): BaseResult<Unit>

    suspend fun refreshChatInfoList()
}