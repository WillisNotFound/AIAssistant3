package com.willis.ai_assistant3.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/16
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected abstract val isLightStatusBar: Boolean
    protected lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = inflateBinding(layoutInflater)
        setContentView(mBinding.root)
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        if (isLightStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.apply {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    protected abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

    protected abstract fun initView()

    protected abstract fun initListener()
}