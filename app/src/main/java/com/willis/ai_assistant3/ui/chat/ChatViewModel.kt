package com.willis.ai_assistant3.ui.chat

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.data.bean.ChatMessage
import com.willis.ai_assistant3.repo.api.IChatMessageRepo
import com.willis.ai_assistant3.repo.impl.ChatMessageRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class ChatViewModel(chatInfoId: Long) : ViewModel() {
    private val mRepo: IChatMessageRepo = ChatMessageRepo(chatInfoId)

    val titleFlow = mRepo.titleFlow
    val loadingFlow = mRepo.loadingFlow
    val newChatMessageFlow = mRepo.newChatMessageFlow

    suspend fun send(content: String) = withContext(Dispatchers.IO) {
        mRepo.send(content)
    }

    suspend fun refresh(): List<ChatMessage> = withContext(Dispatchers.IO) {
        mRepo.getAllChatMessage()
    }
}