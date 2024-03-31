package com.willis.ai_assistant3.ui.setting.spark

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySettingSparkBinding
import com.willis.base.data.BaseResult
import com.willis.base.dialog.EditDialogBuilder
import com.willis.base.ext.collectWhenResumed
import com.willis.base.services.dialogService
import com.willis.base.services.toastService

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class BotSparkSettingActivity : BaseActivity<ActivitySettingSparkBinding>() {
    override val isLightStatusBar = true

    private val mViewModel by viewModels<BotSparkSettingViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivitySettingSparkBinding.inflate(layoutInflater)

    override fun initView() {
        mBinding.settingSparkRouterAppId.apply {
            mViewModel.appIdFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingSparkRouterAppKey.apply {
            mViewModel.apiKeyFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingSparkRouterAppSecret.apply {
            mViewModel.apiSecretFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it)
            }
        }

        mBinding.settingSparkRouterTemperature.apply {
            mViewModel.temperatureFlow.collectWhenResumed(lifecycleScope) {
                setSubtitle(it.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.settingSparkTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.settingSparkRouterAppId.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改（重启应用生效）", mViewModel.appIdFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateAppId(editResult.value))
                }
            }
        }

        mBinding.settingSparkRouterAppKey.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改（重启应用生效）", mViewModel.apiKeyFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateApiKey(editResult.value))
                }
            }
        }

        mBinding.settingSparkRouterAppSecret.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改（重启应用生效）", mViewModel.apiSecretFlow.value)
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success) {
                    simpleHandleResult(mViewModel.updateApiSecret(editResult.value))
                }
            }
        }

        mBinding.settingSparkRouterTemperature.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("修改，范围 (0.0, 1.0]（重启应用生效）", mViewModel.temperatureFlow.value.toString())
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