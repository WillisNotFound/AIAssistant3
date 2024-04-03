package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.ChatMessage
import com.willis.ai_assistant3.data.db.database.UserDatabase
import com.willis.ai_assistant3.repo.api.IChatMessageRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import com.willis.base.services.toastService
import com.willis.base.utils.AppUtils
import com.willis.base.utils.DateUtils
import com.willis.base.utils.NetworkUtils
import com.willis.ernie.Ernie
import com.willis.ernie.ErnieMessage
import com.willis.qwen.Qwen
import com.willis.qwen.QwenMessage
import com.willis.spark.Spark
import com.willis.spark.SparkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/22
 */
class ChatMessageRepo(val chatInfoId: Long) : IChatMessageRepo {
    private val mPhone = appRepo.currentPhoneFlow.value!!
    private lateinit var mChatInfo: ChatInfo

    private val mTitleFlow = MutableStateFlow("")
    override val titleFlow: StateFlow<String> = mTitleFlow

    private val mLoadingFlow = MutableStateFlow(false)
    override val loadingFlow: StateFlow<Boolean> = mLoadingFlow

    private val mNewChatMessageFlow = MutableStateFlow<ChatMessage?>(null)
    override val newChatMessageFlow: StateFlow<ChatMessage?> = mNewChatMessageFlow

    private val mChatInfoDao = UserDatabase.getInstance(mPhone).chatInfoDao()
    private val mChatMessageDao = UserDatabase.getInstance(mPhone).chatMessageDao()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mChatInfo = mChatInfoDao.queryById(chatInfoId)!!
            mTitleFlow.value = mChatInfo.nickname
        }
    }

    override suspend fun send(content: String) {
        if (!NetworkUtils.isNetworkUsable(AppUtils.appContext)) {
            toastService.showError(AppUtils.getString(R.string.app_network_unusable))
            return
        }
        mLoadingFlow.value = true
        val histories = getCorrectHistories(4).toMutableList()
        val newChatMessage = insertAndUpdateNewChatMessage(content, true)
        histories.add(newChatMessage)
        when (mChatInfo.type) {
            0 -> ernieSend(histories)
            1 -> sparkSend(histories)
            2 -> qwenSend(histories)
        }
        mLoadingFlow.value = false
    }

    override suspend fun getAllChatMessage(): List<ChatMessage> {
        mLoadingFlow.value = true
        val list = mChatMessageDao.queryByChatInfoId(mChatInfo.id)
        mLoadingFlow.value = false
        return list
    }

    private suspend fun ernieSend(histories: List<ChatMessage>) {
        val ernieMessages = histories.map {
            ErnieMessage(if (it.request) "user" else "assistant", it.content)
        }
        val settingDao = UserDatabase.getInstance(mPhone).settingErnieDao()
        val globalSetting = settingDao.queryByChatInfoId(-1)!!
        val chatSetting = settingDao.queryByChatInfoId(mChatInfo.id)!!
        val result = Ernie.sendMessage(
            globalSetting.accessToken,
            chatSetting.url,
            chatSetting.temperature,
            ernieMessages
        )
        handleChatResult(result)
    }

    private suspend fun sparkSend(histories: List<ChatMessage>) {
        val sparkMessages = histories.map {
            SparkMessage(if (it.request) "user" else "assistant", it.content)
        }
        val settingDao = UserDatabase.getInstance(mPhone).settingSparkDao()
        val globalSetting = settingDao.queryByChatInfoId(-1)!!
        val chatSetting = settingDao.queryByChatInfoId(mChatInfo.id)!!
        val result = Spark.syncSend(sparkMessages)
        handleChatResult(result)
    }

    private suspend fun qwenSend(histories: List<ChatMessage>) {
        val qwenMessages = histories.map {
            QwenMessage(if (it.request) "user" else "assistant", it.content)
        }
        val settingDao = UserDatabase.getInstance(mPhone).settingQwenDao()
        val globalSetting = settingDao.queryByChatInfoId(-1)!!
        val chatSetting = settingDao.queryByChatInfoId(mChatInfo.id)!!
        val result = Qwen.sendMessage(
            globalSetting.apiKey,
            chatSetting.model,
            chatSetting.temperature,
            chatSetting.enableSearch,
            qwenMessages
        )
        handleChatResult(result)
    }

    private suspend fun insertAndUpdateNewChatMessage(
        content: String,
        request: Boolean
    ): ChatMessage {
        val chatMessage = ChatMessage(
            chatInfoId = mChatInfo.id,
            content = content,
            request = request,
            createMillis = DateUtils.currentMillis()
        )
        chatMessage.id = mChatMessageDao.insert(chatMessage)
        mNewChatMessageFlow.value = chatMessage
        mChatInfo.lastChatMillis = chatMessage.createMillis
        mChatInfo.lastChatMessage = chatMessage.content
        mChatInfoDao.update(mChatInfo)
        return chatMessage
    }

    private suspend fun getCorrectHistories(times: Int): List<ChatMessage> {
        return mChatMessageDao.queryByChatInfoIdDesc(mChatInfo.id).run {
            val list = mutableListOf<ChatMessage>()
            forEach { chatMessage ->
                if (list.size and 1 == 0) {
                    if (!chatMessage.request) list.add(chatMessage)
                } else {
                    if (chatMessage.request) list.add(chatMessage)
                }
            }
            list.takeLast(times * 2).reversed()
        }
    }

    private suspend fun handleChatResult(result: BaseResult<String>) {
        when (result) {
            is BaseResult.Success -> {
                insertAndUpdateNewChatMessage(result.value, false)
            }

            is BaseResult.Failure -> {
                toastService.showError(result.desc)
            }
        }
    }
}