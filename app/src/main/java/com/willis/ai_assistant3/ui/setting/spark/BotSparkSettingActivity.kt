package com.willis.ai_assistant3.ui.setting.spark

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySettingSparkBinding
import com.willis.ai_assistant3.ui.setting.ernie.BotERNIESettingActivity
import com.willis.ai_assistant3.ui.setting.ernie.BotERNIESettingViewModel
import com.willis.ai_assistant3.widget.RouterItem
import com.willis.base.data.BaseResult
import com.willis.base.dialog.EditDialogBuilder
import com.willis.base.ext.collectWhenResumed
import com.willis.base.ext.gone
import com.willis.base.services.dialogService
import com.willis.base.services.toastService

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class BotSparkSettingActivity : BaseActivity<ActivitySettingSparkBinding>() {
    companion object {
        private const val EXTRA_CHAT_INFO_ID = "chat_info_id"
        private const val EXTRA_ENTER_FROM = "enter_from"

        const val ENTER_FROM_CHAT = "chat"
        const val ENTER_FROM_MINE = "mine"

        fun startAction(context: Context, chatInfoId: Long, enterFrom: String) {
            val intent = Intent(context, BotSparkSettingActivity::class.java)
            intent.putExtra(EXTRA_CHAT_INFO_ID, chatInfoId)
            intent.putExtra(EXTRA_ENTER_FROM, enterFrom)
            context.startActivity(intent)
        }
    }

    override val isLightStatusBar = true

    private val mViewModel by viewModels<BotSparkSettingViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BotSparkSettingViewModel(intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0)) as T
            }
        }
    })
    private val mEnterFrom get() = intent.getStringExtra(EXTRA_ENTER_FROM) ?: ENTER_FROM_MINE

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivitySettingSparkBinding.inflate(layoutInflater)

    override fun initView() {
        if (mEnterFrom == BotERNIESettingActivity.ENTER_FROM_CHAT) {
            mBinding.settingSparkTopBar.setTitle("设置")
            mBinding.settingSparkGlobal.gone()
        }

        mBinding.settingSparkRouterAppId.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.appId)
            }
        }

        mBinding.settingSparkRouterApiKey.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.apiKey)
            }
        }

        mBinding.settingSparkRouterApiSecret.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.apiSecret)
            }
        }

        mBinding.settingSparkRouterTemperature.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.temperature.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.settingSparkTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.settingSparkRouterAppId.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.appId?.let {
                    val builder = EditDialogBuilder("修改（重启应用生效）", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateAppId(editResult.value))
                    }
                }
            }
        }

        mBinding.settingSparkRouterApiKey.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.apiKey?.let {
                    val builder = EditDialogBuilder("修改（重启应用生效）", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateApiKey(editResult.value))
                    }
                }
            }
        }

        mBinding.settingSparkRouterApiSecret.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.apiSecret?.let {
                    val builder = EditDialogBuilder("修改（重启应用生效）", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateApiSecret(editResult.value))
                    }
                }
            }
        }

        mBinding.settingSparkRouterTemperature.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.temperature?.let {
                    val builder =
                        EditDialogBuilder("修改，范围 (0.0, 1.0]（重启应用生效）", it.toString())
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