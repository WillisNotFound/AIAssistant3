package com.willis.ai_assistant3.ui.dialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseDialog
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.databinding.DialogCreateChatBinding
import com.willis.ai_assistant3.ui.adapter.CreateChatAdapter
import com.willis.base.ext.addOnTextChangeListener
import com.willis.base.ext.displayMetrics
import com.willis.base.utils.DateUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/20
 */
class CreateChatDialog : BaseDialog<DialogCreateChatBinding>() {
    companion object {
        suspend fun show(fm: FragmentManager): ChatInfo? {
            return suspendCancellableCoroutine { continuation ->
                CreateChatDialog().apply {
                    this.mContinuation = continuation
                }.show(fm, toString())
            }
        }
    }

    var mContinuation: Continuation<ChatInfo?>? = null

    private var mSave: Boolean = false
    private var mType: Int = 1
    private var mNickName: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                decorView.setPadding(0, 0, 0, 0)
                attributes = attributes.apply {
                    width = MATCH_PARENT
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
        if (mSave) {
            mContinuation?.resume(createChatInfo())
        } else {
            mContinuation?.resume(null)
        }
        mContinuation = null
    }

    override fun inflateBinding(layoutInflater: LayoutInflater, container: ViewGroup?): DialogCreateChatBinding {
        return DialogCreateChatBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        mBinding.createChatSpinner.apply {
            adapter = CreateChatAdapter(layoutInflater)
        }

        mBinding.createChatEdtNickname.apply {

        }
    }

    override fun initListener() {
        mBinding.createChatBtnSave.setOnClickListener {
            mSave = true
            dismissAllowingStateLoss()
        }

        mBinding.createChatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mType = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        mBinding.createChatEdtNickname.addOnTextChangeListener {
            mNickName = it?.toString() ?: ""
        }

    }

    private fun createChatInfo() = ChatInfo(
        0,
        mType,
        mNickName,
        DateUtils.currentMillis(),
        DateUtils.currentMillis(),
        ""
    )
}