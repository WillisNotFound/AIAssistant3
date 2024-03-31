package com.willis.ai_assistant3.ui.chat_history

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.repo.api.IChatInfoRepo
import com.willis.ai_assistant3.repo.impl.ChatHistoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class ChatHistoryViewModel : ViewModel() {
    private val mRepo: IChatInfoRepo = ChatHistoryRepo.current.value!!

    val chatInfoListFlow: SharedFlow<List<ChatInfo>> = mRepo.chatInfoListFlow

    suspend fun addChatHistory(chatInfo: ChatInfo) = withContext(Dispatchers.Default) {
        mRepo.addChatInfo(chatInfo)
    }

    suspend fun deleteChatHistory(chatInfoId: Long) = withContext(Dispatchers.Default) {
        mRepo.deleteChatInfo(chatInfoId)
    }

    suspend fun refreshChatHistory() = withContext(Dispatchers.Default) {
        mRepo.refreshChatInfoList()
    }
}