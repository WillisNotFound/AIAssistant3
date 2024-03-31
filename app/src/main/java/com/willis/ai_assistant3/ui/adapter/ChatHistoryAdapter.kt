package com.willis.ai_assistant3.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.databinding.ItemChatHistoryBinding
import com.willis.base.utils.DateUtils

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
class ChatHistoryAdapter : Adapter<ChatHistoryAdapter.ChatHistoryVH>() {
    private val mDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<ChatInfo>() {
        override fun areItemsTheSame(oldItem: ChatInfo, newItem: ChatInfo) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatInfo, newItem: ChatInfo) = oldItem == newItem
    })

    var onItemClickListener: ((View, ChatInfo) -> Unit)? = null
    var onItemLongClickListener: ((View, ChatInfo) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatHistoryBinding.inflate(inflater, parent, false)
        return ChatHistoryVH(binding)
    }

    override fun onBindViewHolder(holder: ChatHistoryVH, position: Int) = holder.bind(mDiffer.currentList[position])

    override fun getItemCount() = mDiffer.currentList.size

    fun updateData(newList: List<ChatInfo>) {
        mDiffer.submitList(newList)
    }

    inner class ChatHistoryVH(private val mBinding: ItemChatHistoryBinding) : ViewHolder(mBinding.root) {
        fun bind(chatInfo: ChatInfo) {
            when (chatInfo.type) {
                0 -> mBinding.itemChatHistoryImvStart.setImageResource(R.drawable.ic_ernie_24dp)
                1 -> mBinding.itemChatHistoryImvStart.setImageResource(R.drawable.ic_spark_24dp)
                2 -> mBinding.itemChatHistoryImvStart.setImageResource(R.drawable.ic_qwen_24dp)
            }
            mBinding.itemChatHistoryTvNickname.text = chatInfo.nickname
            mBinding.itemChatHistoryTvMessage.text = chatInfo.lastChatMessage
            mBinding.itemChatHistoryTvTime.text = DateUtils.getFormatTime(chatInfo.lastChatMillis)
            mBinding.root.setOnClickListener {
                onItemClickListener?.invoke(it, chatInfo)
            }
            mBinding.root.setOnLongClickListener {
                onItemLongClickListener?.invoke(it, chatInfo) ?: false
            }
        }
    }
}