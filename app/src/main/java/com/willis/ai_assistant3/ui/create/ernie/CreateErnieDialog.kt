package com.willis.ai_assistant3.ui.create.ernie

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
import com.willis.ai_assistant3.data.bean.SettingErnie
import com.willis.ai_assistant3.databinding.DialogCreateErnieBinding
import com.willis.base.ext.addOnTextChangeListener
import com.willis.base.ext.displayMetrics
import com.willis.base.utils.DateUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateErnieDialog : BaseDialog<DialogCreateErnieBinding>() {
    companion object {
        suspend fun show(fm: FragmentManager): Boolean {
            return suspendCancellableCoroutine { continuation ->
                CreateErnieDialog().apply {
                    this.mContinuation = continuation
                }.show(fm, toString())
            }
        }
    }

    var mContinuation: Continuation<Boolean>? = null
    private var mCreated: Boolean = false

    private val mViewModel by viewModels<CreateErnieViewModel>()

    private val mType = 0
    private var mNickname = ""
    private var mUrl = ""
//    private var mClientId = ""
//    private var mClientSecret = ""
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
    ): DialogCreateErnieBinding {
        return DialogCreateErnieBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        lifecycleScope.launchWhenResumed {
            mViewModel.getDefaultSetting()?.let {
                mUrl = it.url
//                mClientId = it.clientId
//                mClientSecret = it.clientSecret
                mTemperature = it.temperature
                mBinding.createErnieEdtUrl.setText(it.url)
//                mBinding.createErnieEdtClientId.setText(it.clientId)
//                mBinding.createErnieEdtClientSecret.setText(it.clientSecret)
                mBinding.createErnieEdtTemperature.setText(it.temperature.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.createErnieBtnSave.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val chatInfo = createChatInfo()
                val chatInfoId = mViewModel.createChat(chatInfo)
                val settingErnie = createSettingErnie(chatInfoId)
                mViewModel.createSetting(settingErnie)
                mCreated = true
                dismissAllowingStateLoss()
            }
        }

        mBinding.createErnieEdtNickname.addOnTextChangeListener {
            mNickname = it?.toString() ?: ""
        }

        mBinding.createErnieEdtUrl.addOnTextChangeListener {
            mUrl = it?.toString() ?: ""
        }

//        mBinding.createErnieEdtClientId.addOnTextChangeListener {
//            mClientId = it?.toString() ?: ""
//        }
//
//        mBinding.createErnieEdtClientSecret.addOnTextChangeListener {
//            mClientSecret = it?.toString() ?: ""
//        }

        mBinding.createErnieEdtTemperature.addOnTextChangeListener {
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

    private fun createSettingErnie(chatInfoId: Long) = SettingErnie(
        chatInfoId = chatInfoId,
        url = mUrl,
        clientId = "",
        clientSecret = "",
        accessToken = "",
        temperature = mTemperature
    )
}