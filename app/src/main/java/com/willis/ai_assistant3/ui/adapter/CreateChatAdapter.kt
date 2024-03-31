package com.willis.ai_assistant3.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.databinding.ItemCreateChatBinding


/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/20
 */
class CreateChatAdapter(private val mLayoutInflater: LayoutInflater) : BaseAdapter() {
    private val mList = listOf(
        Item("文心一言", R.drawable.ic_ernie_24dp),
        Item("讯飞星火", R.drawable.ic_spark_24dp),
        Item("阿里千问", R.drawable.ic_qwen_24dp),
    )

    override fun getCount() = mList.size

    override fun getItem(position: Int) = mList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding =
            if (convertView == null) {
                ItemCreateChatBinding.inflate(mLayoutInflater, parent, false)
            } else {
                convertView.tag as ItemCreateChatBinding
            }
        val view = binding.root
        binding.itemCreateChatImv.setImageResource(mList[position].resId)
        binding.itemCreateChatTv.text = mList[position].desc
        view.tag = binding
        return view
    }

    class Item(
        var desc: String,
        val resId: Int
    )
}