package com.willis.ai_assistant3.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.databinding.ActivitySplashBinding
import com.willis.ai_assistant3.ui.login.LoginActivity
import com.willis.ai_assistant3.ui.main.MainActivity
import com.willis.base.ext.collectWhenResumed


/**
 * description: 启动页
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/12
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val isLightStatusBar = false

    private val mViewModel by viewModels<SplashViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivitySplashBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        mViewModel.startCountDown { checkLogin() }
    }

    override fun initView() {
        mBinding.splashBtnSkip.apply {
            mViewModel.countDownFlow.collectWhenResumed(lifecycleScope) {
                this.text = resources.getString(R.string.splash_skip, it)
            }
        }
    }

    override fun initListener() {
        mBinding.splashBtnSkip.setOnClickListener {
            mViewModel.stopCountDown { checkLogin() }
        }
    }

    private fun checkLogin() {
        lifecycleScope.launchWhenResumed {
            if (mViewModel.checkLogin()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}