package com.willis.ai_assistant3.ui.login

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import cn.jiguang.verifysdk.api.JVerificationInterface
import cn.jiguang.verifysdk.api.JVerifyUIConfig
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivityLoginBinding
import com.willis.ai_assistant3.repo.appRepo
import com.willis.ai_assistant3.ui.main.MainActivity
import com.willis.base.data.BaseResult
import com.willis.base.dialog.EditDialogBuilder
import com.willis.base.services.dialogService
import com.willis.base.services.toastService

/**
 * description: 登录页
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/13
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    companion object {
        private const val ADMIN_KEY = "asdfghjkl"
    }

    override val isLightStatusBar = true

    private val mViewModel by viewModels<LoginViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivityLoginBinding.inflate(layoutInflater)

    override fun initView() {

    }

    override fun initListener() {
        mBinding.loginTvAdmin.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val builder = EditDialogBuilder("输入管理员密钥", "")
                val editResult = dialogService.showEditDialog(supportFragmentManager, builder)
                if (editResult is BaseResult.Success && editResult.value == ADMIN_KEY) {
                    handleLoginResult(mViewModel.adminLogin())
                }
            }
        }

        mBinding.loginBtnVerify.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                appRepo.login("10086")
                handleLoginResult(BaseResult.Success("欢迎10086", Unit))
            }
//            when (val result = mViewModel.canLoginAuth()) {
//                is BaseResult.Failure -> {
//                    toastService.showError(result.desc)
//                }
//
//                is BaseResult.Success -> {
//                    loginAuth()
//                }
//            }
        }

        mBinding.loginBtnSms.setOnClickListener {
            when (val result = mViewModel.canSmsLoginAuth()) {
                is BaseResult.Failure -> {
                    toastService.showError(result.desc)
                }

                is BaseResult.Success -> {
                    smsLoginAuth()
                }
            }
        }
    }

    /**
     * 一键登录
     */
    private fun loginAuth() {
        lifecycleScope.launchWhenResumed {
            configVerifyUI()
            handleLoginResult(mViewModel.loginAuth(this@LoginActivity))
        }
    }

    /**
     * 短信登录
     */
    private fun smsLoginAuth() {
        lifecycleScope.launchWhenResumed {
            configVerifyUI()
            handleLoginResult(mViewModel.smsLoginAuth(this@LoginActivity))
        }
    }

    /**
     * 配置登录授权界面UI，[链接](https://docs.jiguang.cn/jverification/client/android_api#jverifyuiconfig-%E4%B8%80%E9%94%AE%E7%99%BB%E5%BD%95-%E9%85%8D%E7%BD%AE%E5%85%83%E7%B4%A0%E8%AF%B4%E6%98%8E)
     */
    private fun configVerifyUI() {
        val config = JVerifyUIConfig.Builder()
            .setDialogTheme(320, 320, 0, 0, false)
            .setLogoOffsetY(48)
            .setLogoHeight(64)
            .setNumFieldOffsetY(112)
            .setNumberFieldHeight(24)
            .setNumberColor(getColor(R.color.blue_200))
            .setSloganOffsetY(136)
            .setLogBtnHeight(48)
            .setLogBtnOffsetY(200)
            .setPrivacyTextCenterGravity(true)
            .setPrivacyMarginB(48)
            .setPrivacyCheckboxSize(12)
            .setPrivacyTextBold(true)
            .setPrivacyUnderlineText(true)
            .build()
        JVerificationInterface.setCustomUIWithConfig(config)
    }

    private fun handleLoginResult(result: BaseResult<Unit>) {
        when (result) {
            is BaseResult.Success -> {
                toastService.showRight(result.desc)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            is BaseResult.Failure -> {
                toastService.showError(result.desc)
            }
        }
    }
}