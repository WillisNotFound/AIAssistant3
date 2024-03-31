package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.ChatMessage
import com.willis.ai_assistant3.data.bean.UserDetail
import com.willis.ai_assistant3.data.db.database.AppDatabase
import com.willis.ai_assistant3.data.db.database.ChatDatabase
import com.willis.ai_assistant3.repo.api.IChatMessageRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import com.willis.base.services.toastService
import com.willis.base.utils.AppUtils
import com.willis.base.utils.DateUtils
import com.willis.base.utils.NetworkUtils
import com.willis.ernie.Ernie
import com.willis.ernie.ErnieQuestion
import com.willis.qwen.Qwen
import com.willis.qwen.QwenQuestion
import com.willis.spark.Spark
import com.willis.spark.SparkQuestion
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
class ChatMessageRepo(private val chatInfoId: Long) : IChatMessageRepo {
    private val mPhone = appRepo.currentPhoneFlow.value!!
    private lateinit var mChatInfo: ChatInfo
    private lateinit var mUserDetail: UserDetail

    private val mTitleFlow = MutableStateFlow("")
    override val titleFlow: StateFlow<String> = mTitleFlow

    private val mLoadingFlow = MutableStateFlow(false)
    override val loadingFlow: StateFlow<Boolean> = mLoadingFlow

    private val mNewChatMessageFlow = MutableStateFlow<ChatMessage?>(null)
    override val newChatMessageFlow: StateFlow<ChatMessage?> = mNewChatMessageFlow

    private val mUserDetailDao = AppDatabase.instance.userDetailDao()
    private val mChatInfoDao = ChatDatabase.getInstance(mPhone).chatInfoDao()
    private val mChatMessageDao = ChatDatabase.getInstance(mPhone).chatMessageDao()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mChatInfo = mChatInfoDao.queryById(chatInfoId)!!
            mUserDetail = mUserDetailDao.queryByPhone(mPhone)!!
            mTitleFlow.value = mChatInfo.nickname
        }
    }

    override suspend fun send(content: String) {
        if (!NetworkUtils.isNetworkUsable(AppUtils.appContext)) {
            toastService.showError(AppUtils.getString(R.string.app_network_unusable))
            return
        }
        mLoadingFlow.value = true
        insertAndUpdateNewChatMessage(content, true)
        val historyChatMessages = mChatMessageDao.queryByChatInfoId(mChatInfo.id, 0, 20).reversed()
        when (mChatInfo.type) {
            0 -> ernieSend(historyChatMessages)
            1 -> sparkSend(historyChatMessages)
            2 -> qwenSend(historyChatMessages)
        }
        mLoadingFlow.value = false
    }

    override suspend fun getAllChatMessage(): List<ChatMessage> {
        mLoadingFlow.value = true
        val list = mChatMessageDao.queryByChatInfoId(mChatInfo.id)
        mLoadingFlow.value = false
        return list
    }

    private suspend fun ernieSend(historyChatMessages: List<ChatMessage>) {
        val ernieQuestion = historyChatMessages.map {
            ErnieQuestion(if (it.request) "user" else "assistant", it.content)
        }
        handleChatResult(
            Ernie.syncSend(
                mUserDetail.ernieAccessToken,
                mUserDetail.ernieUrl,
                mUserDetail.ernieTemperature,
                ernieQuestion
            )
        )
    }

    private suspend fun sparkSend(historyChatMessages: List<ChatMessage>) {
        val sparkQuestions = historyChatMessages.map {
            SparkQuestion(if (it.request) "user" else "assistant", it.content)
        }
        handleChatResult(Spark.syncSend(sparkQuestions))
    }

    private suspend fun qwenSend(historyChatMessages: List<ChatMessage>) {
        val qwenQuestions = historyChatMessages.map {
            QwenQuestion(if (it.request) "user" else "assistant", it.content)
        }
        val temperature = mUserDetail.qwenTemperature
        handleChatResult(Qwen.syncSend(mUserDetail.qwenApiKey, mUserDetail.qwenModel, temperature, qwenQuestions))
    }

    private suspend fun insertAndUpdateNewChatMessage(content: String, request: Boolean) {
        val chatMessage = ChatMessage(
            0,
            mChatInfo.id,
            content,
            request,
            DateUtils.currentMillis()
        )
        chatMessage.id = mChatMessageDao.insert(chatMessage)
        mNewChatMessageFlow.value = chatMessage
        mChatInfo.lastChatMillis = chatMessage.createMillis
        mChatInfo.lastChatMessage = chatMessage.content
        mChatInfoDao.update(mChatInfo)
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