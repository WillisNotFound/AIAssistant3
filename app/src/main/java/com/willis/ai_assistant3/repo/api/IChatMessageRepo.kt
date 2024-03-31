package com.willis.ai_assistant3.repo.api

import com.willis.ai_assistant3.data.bean.ChatMessage
import kotlinx.coroutines.flow.StateFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/22
 */
interface IChatMessageRepo {
    val titleFlow: StateFlow<String>

    val loadingFlow: StateFlow<Boolean>

    val newChatMessageFlow: StateFlow<ChatMessage?>

    suspend fun send(content: String)

    suspend fun getAllChatMessage(): List<ChatMessage>
}