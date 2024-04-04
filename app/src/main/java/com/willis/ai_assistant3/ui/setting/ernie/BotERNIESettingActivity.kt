package com.willis.ai_assistant3.ui.setting.ernie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.databinding.ActivitySettingErnieBinding
import com.willis.ai_assistant3.ui.chat.ChatActivity
import com.willis.ai_assistant3.ui.chat.ChatViewModel
import com.willis.ai_assistant3.widget.RouterItem
import com.willis.base.data.BaseResult
import com.willis.base.dialog.ConfirmDialogBuilder
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
class BotERNIESettingActivity : BaseActivity<ActivitySettingErnieBinding>() {
    companion object {
        private const val EXTRA_CHAT_INFO_ID = "chat_info_id"
        private const val EXTRA_ENTER_FROM = "enter_from"

        const val ENTER_FROM_CHAT = "chat"
        const val ENTER_FROM_MINE = "mine"

        private const val INFORMATION_CLIENT_ID = "应用的API Key，在智能云千帆控制台-应用列表查看"
        private const val INFORMATION_CLIENT_SECRET = "应用的Secret Key，在智能云千帆控制台-应用列表查看"
        private const val INFORMATION_ACCESS_TOKEN = "应用的访问凭证，由CLIENT ID与CLIENT SECRET共同决定，默认有效期30天，需要注意及时刷新"
        private const val INFORMATION_URL = "模型的URL，不同模型对应不同的URL"
        private const val INFORMATION_TEMPERATURE = "较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定\n范围: (0, 1.0]，不能为0"
        private const val INFORMATION_CONTEXT_TIMES = "每次对话最多带上的上下文对话数\n注意：上下文越多，响应越慢"


        fun startAction(context: Context, chatInfoId: Long, enterFrom: String) {
            val intent = Intent(context, BotERNIESettingActivity::class.java)
            intent.putExtra(EXTRA_CHAT_INFO_ID, chatInfoId)
            intent.putExtra(EXTRA_ENTER_FROM, enterFrom)
            context.startActivity(intent)
        }
    }

    override val isLightStatusBar = true

    private val mViewModel by viewModels<BotERNIESettingViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BotERNIESettingViewModel(intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0)) as T
            }
        }
    })
    private val mEnterFrom get() = intent.getStringExtra(EXTRA_ENTER_FROM) ?: ENTER_FROM_MINE

    override fun inflateBinding(layoutInflater: LayoutInflater) =
        ActivitySettingErnieBinding.inflate(layoutInflater)

    override fun initView() {
        if (mEnterFrom == ENTER_FROM_CHAT) {
            mBinding.settingErnieTopBar.setTitle("设置")
            mBinding.settingErnieGlobal.gone()
        }

        mBinding.settingErnieRouterClientId.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.clientId)
            }
        }

        mBinding.settingErnieRouterClientSecret.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.clientSecret)
            }
        }

        mBinding.settingErnieRouterAccessToken.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.accessToken)
            }
        }

        mBinding.settingErnieRouterUrl.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.url)
            }
        }

        mBinding.settingErnieRouterTemperature.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.temperature.toString())
            }
        }

        mBinding.settingErnieRouterContextTimes.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.contextTimes.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.settingErnieTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.settingErnieRouterClientId.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.clientId?.let {
                    val builder = EditDialogBuilder("修改", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateClientId(editResult.value))
                    }
                }
            }
        }

        mBinding.settingErnieRouterClientId.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("CLIENT ID", INFORMATION_CLIENT_ID)
                )
            }
        }

        mBinding.settingErnieRouterClientSecret.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.clientSecret?.let {
                    val builder = EditDialogBuilder("修改", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateClientSecret(editResult.value))
                    }
                }
            }
        }

        mBinding.settingErnieRouterClientSecret.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("CLIENT SECRET", INFORMATION_CLIENT_SECRET)
                )
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

        mBinding.settingErnieRouterAccessToken.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("ACCESS TOKEN", INFORMATION_ACCESS_TOKEN)
                )
            }
        }

        mBinding.settingErnieRouterUrl.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.url?.let {
                    val builder = EditDialogBuilder("修改", it)
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateUrl(editResult.value))
                    }
                }
            }
        }

        mBinding.settingErnieRouterUrl.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("URL", INFORMATION_URL)
                )
            }
        }

        mBinding.settingErnieRouterTemperature.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.temperature?.let {
                    val builder = EditDialogBuilder("修改，范围 (0.0, 1.0]", it.toString())
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateTemperature(editResult.value))
                    }
                }
            }
        }

        mBinding.settingErnieRouterTemperature.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("TEMPERATURE", INFORMATION_TEMPERATURE)
                )
            }
        }

        mBinding.settingErnieRouterContextTimes.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                mViewModel.state.value?.contextTimes?.let {
                    val builder = EditDialogBuilder("修改", it.toString())
                    val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                    if (editResult is BaseResult.Success) {
                        simpleHandleResult(mViewModel.updateContextTimes(editResult.value))
                    }
                }
            }
        }

        mBinding.settingErnieRouterContextTimes.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("CONTEXT TIMES", INFORMATION_CONTEXT_TIMES)
                )
            }
        }
    }

    private fun simpleHandleResult(result: BaseResult<Unit>) {
        when (result) {
            is BaseResult.Failure -> toastService.showError(result.desc)
            is BaseResult.Success -> toastService.showRight(result.desc)
        }
    }

    private fun buildConfirmDialog(title: String, information: String): ConfirmDialogBuilder {
        return ConfirmDialogBuilder(
            title = title,
            content = information,
            showCancelButton = false
        )
    }
}