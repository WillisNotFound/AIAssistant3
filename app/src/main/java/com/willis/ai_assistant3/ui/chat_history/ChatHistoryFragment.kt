package com.willis.ai_assistant3.ui.chat_history

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseFragment
import com.willis.ai_assistant3.databinding.FragmentChatHistoryBinding
import com.willis.ai_assistant3.ui.adapter.ChatHistoryAdapter
import com.willis.ai_assistant3.ui.chat.ChatActivity
import com.willis.ai_assistant3.ui.dialog.CreateChatDialog
import com.willis.ai_assistant3.ui.dialog.DeleteChatPopup
import com.willis.base.data.BaseResult
import com.willis.base.dialog.ConfirmDialogBuilder
import com.willis.base.ext.collectWhenResumed
import com.willis.base.services.dialogService
import com.willis.base.services.toastService

/**
 * description: 对话历史页
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class ChatHistoryFragment : BaseFragment<FragmentChatHistoryBinding>() {
    private val mViewModel by viewModels<ChatHistoryViewModel>()
    private lateinit var mAdapter: ChatHistoryAdapter

    override fun inflateBinding(layoutInflater: LayoutInflater) = FragmentChatHistoryBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenStarted {
            mViewModel.refreshChatHistory()
        }
    }

    override fun initView() {
        mBinding.chatHistoryRecyclerview.apply {
            mAdapter = ChatHistoryAdapter()
            adapter = mAdapter
        }

        mViewModel.chatInfoListFlow.collectWhenResumed(lifecycleScope) {
            mAdapter.updateData(it)
            if (mBinding.chatHistoryRefresh.isRefreshing) {
                mBinding.chatHistoryRefresh.isRefreshing = false
            }
        }
    }

    override fun initListener() {
        mBinding.chatHistoryRefresh.setOnRefreshListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.refreshChatHistory()
            }
        }

        mBinding.chatHistoryFabAdd.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val chatInfo = CreateChatDialog.show(childFragmentManager)
                if (chatInfo != null) mViewModel.addChatHistory(chatInfo)
            }
        }

        mAdapter.onItemClickListener = { view, chatInfo ->

            ChatActivity.startAction(requireContext(), chatInfo)
        }

        mAdapter.onItemLongClickListener = { view, chatInfo ->
            lifecycleScope.launchWhenResumed {
                val builder = ConfirmDialogBuilder(
                    resources.getString(R.string.chat_history_tip),
                    resources.getString(R.string.chat_history_tip_delete_chat, chatInfo.nickname)
                )
                if (DeleteChatPopup.showOnAnchor(requireContext(), view)
                    && dialogService.showConfirmDialog(childFragmentManager, builder) == true
                ) {
                    simpleHandleResult(mViewModel.deleteChatHistory(chatInfo.id))
                }
            }
            true
        }
    }

    private fun simpleHandleResult(result: BaseResult<Unit>) {
        when (result) {
            is BaseResult.Failure -> toastService.showError(result.desc)
            is BaseResult.Success -> toastService.showRight(result.desc)
        }
    }
}