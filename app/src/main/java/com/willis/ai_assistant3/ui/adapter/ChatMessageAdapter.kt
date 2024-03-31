package com.willis.ai_assistant3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.data.bean.ChatMessage
import com.willis.ai_assistant3.databinding.ItemChatMessageLeftBinding
import com.willis.ai_assistant3.databinding.ItemChatMessageRightBinding
import com.willis.base.utils.DateUtils
import com.willis.base.utils.KeyboardUtils

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/26
 */
class ChatMessageAdapter(private val mChatInfoType: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_LEFT = 0
        private const val TYPE_RIGHT = 1
    }

    private val mList = mutableListOf<ChatMessage>()

    fun refreshData(list: List<ChatMessage>) {
        mList.clear()
        mList.addAll(list)
    }

    fun appendData(chatMessage: ChatMessage) {
        mList.add(chatMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LEFT -> LeftVH(ItemChatMessageLeftBinding.inflate(layoutInflater, parent, false))
            TYPE_RIGHT -> RightVH(ItemChatMessageRightBinding.inflate(layoutInflater, parent, false))
            else -> throw RuntimeException("Error view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeftVH -> holder.bind(mList[position])
            is RightVH -> holder.bind(mList[position])
            else -> throw RuntimeException("Error holder type: $holder")
        }
    }

    override fun getItemViewType(position: Int) = if (mList[position].request) TYPE_RIGHT else TYPE_LEFT

    override fun getItemCount() = mList.size

    inner class LeftVH(private val mBinding: ItemChatMessageLeftBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(chatMessage: ChatMessage) {
            mBinding.itemChatMessageLeftTvContent.text = chatMessage.content
            mBinding.itemChatMessageLeftTvTime.text = DateUtils.getFormatTime(chatMessage.createMillis)
            when (mChatInfoType) {
                0 -> mBinding.itemChatMessageLeftImvAvatar.setImageResource(R.drawable.ic_ernie_24dp)
                1 -> mBinding.itemChatMessageLeftImvAvatar.setImageResource(R.drawable.ic_spark_24dp)
                2 -> mBinding.itemChatMessageLeftImvAvatar.setImageResource(R.drawable.ic_qwen_24dp)
            }
        }
    }

    inner class RightVH(private val mBinding: ItemChatMessageRightBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(chatMessage: ChatMessage) {
            mBinding.itemChatMessageRightTvContent.text = chatMessage.content
            mBinding.itemChatMessageRightTvTime.text = DateUtils.getFormatTime(chatMessage.createMillis)
        }
    }
}