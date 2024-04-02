package com.willis.ai_assistant3.ui.create.spark

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseDialog
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.SettingSpark
import com.willis.ai_assistant3.databinding.DialogCreateSparkBinding
import com.willis.base.ext.addOnTextChangeListener
import com.willis.base.ext.displayMetrics
import com.willis.base.utils.DateUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateSparkDialog : BaseDialog<DialogCreateSparkBinding>() {
    companion object {
        suspend fun show(fm: FragmentManager): Boolean {
            return suspendCancellableCoroutine { continuation ->
                CreateSparkDialog().apply {
                    this.mContinuation = continuation
                }.show(fm, toString())
            }
        }
    }

    var mContinuation: Continuation<Boolean>? = null
    private var mCreated: Boolean = false

    private val mViewModel by viewModels<CreateSparkViewModel>()

    private val mType = 1
    private var mNickname = ""
    private var mAppId = ""
    private var mApiKey = ""
    private var mApiSecret = ""
    private var mTemperature = 0.8F

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                decorView.setPadding(0, 0, 0, 0)
                attributes = attributes.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = (displayMetrics.heightPixels * 0.8).toInt()
                    gravity = Gravity.BOTTOM
                    windowAnimations = R.style.Base_BottomDialogAnimation
                    dimAmount = 0.5F
                }
                setBackgroundDrawable(ColorDrawable())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mContinuation?.resume(mCreated)
        mContinuation = null
    }

    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): DialogCreateSparkBinding {
        return DialogCreateSparkBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        lifecycleScope.launchWhenResumed {
            mViewModel.getDefaultSetting()?.let {
                mAppId = it.appId
                mApiKey = it.apiKey
                mApiSecret= it.apiSecret
                mTemperature = it.temperature
                mBinding.createSparkEdtAppId.setText(it.appId)
                mBinding.createSparkEdtApiKey.setText(it.apiKey)
                mBinding.createSparkEdtApiSecret.setText(it.apiSecret)
                mBinding.createSparkEdtTemperature.setText(it.temperature.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.createSparkBtnSave.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val chatInfo = createChatInfo()
                val chatInfoId = mViewModel.createChat(chatInfo)
                val settingErnie = createSettingSpark(chatInfoId)
                mViewModel.createSetting(settingErnie)
                mCreated = true
                dismissAllowingStateLoss()
            }
        }

        mBinding.createSparkEdtAppId.addOnTextChangeListener {
            mAppId = it?.toString() ?: ""
        }

        mBinding.createSparkEdtApiKey.addOnTextChangeListener {
            mApiKey = it?.toString() ?: ""
        }

        mBinding.createSparkEdtApiSecret.addOnTextChangeListener {
            mApiSecret = it?.toString() ?: ""
        }

        mBinding.createSparkEdtTemperature.addOnTextChangeListener {
            mTemperature = it?.toString()?.toFloatOrNull() ?: 0.8F
        }
    }

    private fun createChatInfo() = ChatInfo(
        type = mType,
        nickname = mNickname,
        createMillis = DateUtils.currentMillis(),
        lastChatMillis = DateUtils.currentMillis(),
        lastChatMessage = ""
    )

    private fun createSettingSpark(chatInfoId: Long) = SettingSpark(
        chatInfoId = chatInfoId,
        appId = mAppId,
        apiKey = mApiKey,
        apiSecret = mApiSecret,
        temperature = mTemperature
    )
}