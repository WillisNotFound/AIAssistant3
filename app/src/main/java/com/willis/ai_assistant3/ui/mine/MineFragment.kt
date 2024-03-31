package com.willis.ai_assistant3.ui.mine

import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseFragment
import com.willis.ai_assistant3.databinding.FragmentMineBinding
import com.willis.ai_assistant3.ui.setting.account.AccountSettingActivity
import com.willis.ai_assistant3.ui.setting.ernie.BotERNIESettingActivity
import com.willis.ai_assistant3.ui.setting.qwen.BotQwenSettingActivity
import com.willis.ai_assistant3.ui.setting.spark.BotSparkSettingActivity
import com.willis.base.ext.collectWhenResumed
import com.willis.base.services.toastService

/**
 * description: 我的页面
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {
    private val mViewModel by viewModels<MineViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = FragmentMineBinding.inflate(layoutInflater)

    override fun initView() {
        mBinding.mineRouterAccount.apply {
            mViewModel.mPhoneFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }
    }

    override fun initListener() {
        mBinding.mineRouterAccount.setOnClickListener {
            startActivity(Intent(requireActivity(), AccountSettingActivity::class.java))
        }

        mBinding.mineRouterErnie.setOnClickListener {
            startActivity(Intent(requireActivity(), BotERNIESettingActivity::class.java))
        }

        mBinding.mineRouterSpark.setOnClickListener {
            startActivity(Intent(requireActivity(), BotSparkSettingActivity::class.java))
        }

        mBinding.mineRouterQwen.setOnClickListener {
            startActivity(Intent(requireActivity(), BotQwenSettingActivity::class.java))
        }

        mBinding.mineRouterAbout.setOnClickListener {
            toastService.showNormal(resources.getString(R.string.mine_about))
        }
    }
}