package com.willis.ai_assistant3.ui.setting.account

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySettingAccountBinding
import com.willis.ai_assistant3.ui.login.LoginActivity
import com.willis.base.data.BaseResult
import com.willis.base.dialog.EditDialogBuilder
import com.willis.base.ext.collectWhenResumed
import com.willis.base.services.dialogService

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/29
 */
class AccountSettingActivity : BaseActivity<ActivitySettingAccountBinding>() {
    override val isLightStatusBar: Boolean = true

    private val mViewModel by viewModels<AccountSettingViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivitySettingAccountBinding.inflate(layoutInflater)

    override fun initView() {
        mBinding.settingAccountRouterPhone.apply {
            mViewModel.phoneFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingAccountRouterNickname.apply {
            mViewModel.nicknameFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingAccountRouterCreateTime.apply {
            mViewModel.createTimeFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }
    }

    override fun initListener() {
        mBinding.settingAccountTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.settingAccountRouterNickname.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改", mViewModel.nicknameFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) mViewModel.updateNickname(editResult.value)
            }
        }

        mBinding.settingAccountBtnExit.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.exit()
                clearHistoryAndStartLogin()
            }
        }
    }

    private fun clearHistoryAndStartLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}