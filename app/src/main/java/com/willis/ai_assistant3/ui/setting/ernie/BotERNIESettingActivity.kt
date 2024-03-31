package com.willis.ai_assistant3.ui.setting.ernie

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySettingErnieBinding
import com.willis.base.data.BaseResult
import com.willis.base.dialog.ConfirmDialogBuilder
import com.willis.base.dialog.EditDialogBuilder
import com.willis.base.ext.collectWhenResumed
import com.willis.base.services.dialogService
import com.willis.base.services.toastService

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class BotERNIESettingActivity : BaseActivity<ActivitySettingErnieBinding>() {
    override val isLightStatusBar = true

    private val mViewModel by viewModels<BotERNIESettingViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivitySettingErnieBinding.inflate(layoutInflater)

    override fun initView() {
        mBinding.settingErnieRouterClientId.apply {
            mViewModel.clientIdFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingErnieRouterClientSecret.apply {
            mViewModel.clientSecretFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingErnieRouterAccessToken.apply {
            mViewModel.accessTokenFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingErnieRouterUrl.apply {
            mViewModel.urlFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingErnieRouterTemperature.apply {
            mViewModel.temperatureFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.settingErnieTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.settingErnieRouterClientId.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改", mViewModel.clientIdFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateClientId(editResult.value))
                }
            }
        }

        mBinding.settingErnieRouterClientSecret.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改", mViewModel.clientSecretFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateClientSecret(editResult.value))
                }
            }
        }

        mBinding.settingErnieRouterAccessToken.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = ConfirmDialogBuilder("提示", "是否刷新 Access Token")
                if (dialogService.showConfirmDialog(supportFragmentManager, builder) == true) {
                    simpleHandleResult(mViewModel.refreshAccessToken())
                }
            }
        }

        mBinding.settingErnieRouterUrl.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改", mViewModel.urlFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateUrl(editResult.value))
                }
            }
        }

        mBinding.settingErnieRouterTemperature.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改，范围 (0.0, 1.0]", mViewModel.temperatureFlow.value.toString())
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateTemperature(editResult.value))
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