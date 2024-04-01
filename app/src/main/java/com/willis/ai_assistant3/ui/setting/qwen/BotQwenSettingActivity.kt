package com.willis.ai_assistant3.ui.setting.qwen

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySettingQwenBinding
import com.willis.base.data.BaseResult
import com.willis.base.dialog.EditDialogBuilder
import com.willis.base.ext.collectWhenResumed
import com.willis.base.services.dialogService
import com.willis.base.services.toastService

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
class BotQwenSettingActivity : BaseActivity<ActivitySettingQwenBinding>() {
    override val isLightStatusBar: Boolean = true

    private val mViewModel by viewModels<BotQwenSettingViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivitySettingQwenBinding.inflate(layoutInflater)

    override fun initView() {
        mBinding.settingQwenRouterApiKey.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.apiKey)
            }
        }

        mBinding.settingQwenRouterModel.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.model)
            }
        }

        mBinding.settingQwenRouterTemperature.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.temperature.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.settingQwenTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.settingQwenRouterApiKey.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.apiKey?.let {
                    val builder = EditDialogBuilder("修改", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateApiKey(editResult.value))
                    }
                }
            }
        }

        mBinding.settingQwenRouterModel.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.model?.let {
                    val builder = EditDialogBuilder("修改", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateModel(editResult.value))
                    }
                }
            }
        }

        mBinding.settingQwenRouterTemperature.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.temperature?.let {
                    val builder = EditDialogBuilder("修改，范围 [0.0, 2.0)", it.toString())
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateTemperature(editResult.value))
                    }
                }
            }
        }
    }

    private fun simpleHandleResult(result: BaseResult<Unit>) {
        when (result) {
            is BaseResult.Failure -> toastService.showError(result.desc)
            is BaseResult.Success -> toastService.showRight(result.desc)
        }
    }
}