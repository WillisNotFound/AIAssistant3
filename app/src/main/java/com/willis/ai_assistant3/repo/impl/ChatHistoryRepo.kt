package com.willis.ai_assistant3.repo.impl

import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.db.database.ChatDatabase
import com.willis.ai_assistant3.repo.api.IChatInfoRepo
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import com.willis.base.ext.mapToStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/20
 */
class ChatHistoryRepo private constructor(phone: String) : IChatInfoRepo {
    companion object {
        val current: StateFlow<IChatInfoRepo?> = appRepo.currentPhoneFlow.mapToStateFlow {
            if (it != null) ChatHistoryRepo(it) else null
        }
    }

    private val mChatInfoDao = ChatDatabase.getInstance(phone).chatInfoDao()

    private val mChatInfoListFlow = MutableSharedFlow<List<ChatInfo>>(1, 1)
    override val chatInfoListFlow: SharedFlow<List<ChatInfo>> = mChatInfoListFlow

    override suspend fun addChatInfo(chatInfo: ChatInfo): BaseResult<Unit> {
        if (mChatInfoDao.insert(chatInfo) < 1) return BaseResult.Failure("创建失败")
        refreshChatInfoList()
        return BaseResult.Success("创建成功", Unit)
    }

    override suspend fun deleteChatInfo(chatInfoId: Long): BaseResult<Unit> {
        mChatInfoDao.deleteById(chatInfoId)
        refreshChatInfoList()
        return BaseResult.Success("删除成功", Unit)
    }

    override suspend fun refreshChatInfoList() {
        val list = mChatInfoDao.queryAll()
        mChatInfoListFlow.emit(list)
    }
}