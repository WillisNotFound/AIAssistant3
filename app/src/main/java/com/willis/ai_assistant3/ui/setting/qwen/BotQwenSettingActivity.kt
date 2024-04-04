package com.willis.ai_assistant3.ui.setting.qwen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySettingQwenBinding
import com.willis.ai_assistant3.ui.setting.ernie.BotERNIESettingActivity
import com.willis.ai_assistant3.ui.setting.ernie.BotERNIESettingViewModel
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
 * @date: 2023/12/28
 */
class BotQwenSettingActivity : BaseActivity<ActivitySettingQwenBinding>() {
    companion object {
        private const val EXTRA_CHAT_INFO_ID = "chat_info_id"
        private const val EXTRA_ENTER_FROM = "enter_from"

        const val ENTER_FROM_CHAT = "chat"
        const val ENTER_FROM_MINE = "mine"

        private const val INFORMATION_API_KEY = "访问模型服务灵积的密钥"
        private const val INFORMATION_MODEL = "指定用于对话的通义千问模型名"
        private const val INFORMATION_ENABLE_SEARCH =
            "模型内置了互联网搜索服务，该参数控制模型在生成文本时是否参考使用互联网搜索结果"
        private const val INFORMATION_TEMPERATURE =
            "较高的temperature值会降低概率分布的峰值，使得更多的低概率词被选择，生成结果更加多样化；而较低的temperature值则会增强概率分布的峰值，使得高概率词更容易被选择，生成结果更加确定。\n取值范围：[0, 2)，系统默认值0.85。不建议取值为0，无意义。"
        private const val INFORMATION_CONTEXT_TIMES =
            "每次对话最多带上的上下文对话数\n注意：上下文越多，响应越慢"

        fun startAction(context: Context, chatInfoId: Long, enterFrom: String) {
            val intent = Intent(context, BotQwenSettingActivity::class.java)
            intent.putExtra(EXTRA_CHAT_INFO_ID, chatInfoId)
            intent.putExtra(EXTRA_ENTER_FROM, enterFrom)
            context.startActivity(intent)
        }
    }

    override val isLightStatusBar: Boolean = true

    private val mViewModel by viewModels<BotQwenSettingViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BotQwenSettingViewModel(intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0)) as T
            }
        }
    })
    private val mEnterFrom get() = intent.getStringExtra(EXTRA_ENTER_FROM) ?: ENTER_FROM_MINE

    override fun inflateBinding(layoutInflater: LayoutInflater) =
        ActivitySettingQwenBinding.inflate(layoutInflater)

    override fun initView() {
        if (mEnterFrom == BotERNIESettingActivity.ENTER_FROM_CHAT) {
            mBinding.settingQwenTopBar.setTitle("设置")
            mBinding.settingQwenGlobal.gone()
        }

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

        mBinding.settingQwenSwitchEnableSearch.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                isChecked = it?.enableSearch ?: false
            }
        }

        mBinding.settingQwenRouterContextTimes.apply {
            mViewModel.state.collectWhenResumed(lifecycleScope) {
                setSubtitle(it?.contextTimes.toString())
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

        mBinding.settingQwenRouterApiKey.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("CLIENT ID", INFORMATION_API_KEY)
                )
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

        mBinding.settingQwenRouterModel.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("MODEL", INFORMATION_MODEL)
                )
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

        mBinding.settingQwenRouterTemperature.setOnTitleEndImvClick {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("TEMPERATURE", INFORMATION_TEMPERATURE)
                )
            }
        }

        mBinding.settingQwenSwitchEnableSearch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launchWhenResumed {
                mViewModel.updateEnableSearch(isChecked)
            }
        }

        mBinding.settingQwenImvEnableSearch.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                dialogService.showConfirmDialog(
                    supportFragmentManager,
                    buildConfirmDialog("ENABLE SEARCH", INFORMATION_ENABLE_SEARCH)
                )
            }
        }

        mBinding.settingQwenRouterContextTimes.setOnClickListener {
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

        mBinding.settingQwenRouterContextTimes.setOnTitleEndImvClick {
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