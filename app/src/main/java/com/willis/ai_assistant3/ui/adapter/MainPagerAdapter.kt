package com.willis.ai_assistant3.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.willis.ai_assistant3.ui.chat_history.ChatHistoryFragment
import com.willis.ai_assistant3.ui.mine.MineFragment

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/16
 */
class MainPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val mFragments = listOf<Fragment>(ChatHistoryFragment(), MineFragment())

    override fun getItemCount() = mFragments.size

    override fun createFragment(position: Int) = mFragments[position]
}