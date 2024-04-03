package com.willis.ai_assistant3.ui.create.qwen

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
import com.willis.ai_assistant3.data.bean.SettingQwen
import com.willis.ai_assistant3.databinding.DialogCreateQwenBinding
import com.willis.base.ext.addOnTextChangeListener
import com.willis.base.ext.displayMetrics
import com.willis.base.utils.DateUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateQwenDialog: BaseDialog<DialogCreateQwenBinding>() {
    companion object {
        suspend fun show(fm: FragmentManager): Boolean {
            return suspendCancellableCoroutine { continuation ->
                CreateQwenDialog().apply {
                    this.mContinuation = continuation
                }.show(fm, toString())
            }
        }
    }

    var mContinuation: Continuation<Boolean>? = null
    private var mCreated: Boolean = false

    private val mViewModel by viewModels<CreateQwenViewModel>()

    private val mType = 2
    private var mNickname = ""
    private var mModel = ""
    private var mTemperature = 0.8F
    private var mEnableSearch = false
    private var mContextTimes = 4

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
    ): DialogCreateQwenBinding {
        return DialogCreateQwenBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        lifecycleScope.launchWhenResumed {
            mViewModel.getDefaultSetting()?.let {
//                mApiKey = it.apiKey
                mModel = it.model
                mTemperature = it.temperature
                mBinding.createQwenEdtApiKey.setText(it.apiKey)
                mBinding.createQwenEdtModel.setText(it.model)
                mBinding.createQwenEdtTemperature.setText(it.temperature.toString())
                mBinding.createQwenSwitchEnableSearch.isChecked = it.enableSearch
                mBinding.createQwenEdtContextTimes.setText(it.contextTimes.toString())
            }
        }
    }

    override fun initListener() {
        mBinding.createQwenBtnSave.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val chatInfo = createChatInfo()
                val chatInfoId = mViewModel.createChat(chatInfo)
                val settingErnie = createSettingQwen(chatInfoId)
                mViewModel.createSetting(settingErnie)
                mCreated = true
                dismissAllowingStateLoss()
            }
        }

        mBinding.createQwenEdtNickname.addOnTextChangeListener {
            mNickname = it?.toString() ?: ""
        }

        mBinding.createQwenEdtModel.addOnTextChangeListener {
            mModel = it?.toString() ?: ""
        }

        mBinding.createQwenEdtTemperature.addOnTextChangeListener {
            mTemperature = it?.toString()?.toFloatOrNull() ?: 0.8F
        }

        mBinding.createQwenSwitchEnableSearch.setOnCheckedChangeListener { _, isChecked ->
            mEnableSearch = isChecked
        }

        mBinding.createQwenEdtContextTimes.addOnTextChangeListener {
            mContextTimes = it?.toString()?.toIntOrNull() ?: 4
        }
    }

    private fun createChatInfo() = ChatInfo(
        type = mType,
        nickname = mNickname,
        createMillis = DateUtils.currentMillis(),
        lastChatMillis = DateUtils.currentMillis(),
        lastChatMessage = ""
    )

    private fun createSettingQwen(chatInfoId: Long) = SettingQwen(
        chatInfoId = chatInfoId,
        apiKey = "",
        model = mModel,
        temperature = mTemperature,
        enableSearch = mEnableSearch,
        contextTimes = mContextTimes
    )
}