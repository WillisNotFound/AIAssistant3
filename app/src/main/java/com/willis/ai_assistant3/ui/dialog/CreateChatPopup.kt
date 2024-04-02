package com.willis.ai_assistant3.ui.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.databinding.PopupCreateChatBinding
import com.willis.base.ext.dp
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateChatPopup(context: Context) : PopupWindow(context) {
    companion object {
        suspend fun showOnAnchor(context: Context, anchor: View): Int {
            return suspendCancellableCoroutine<Int> { continuation ->
                CreateChatPopup(context).apply {
                    this.mContinuation = continuation
                }.showAsDropDown(anchor, 0, -(212.dp))
            }
        }
    }

    private var _binding: PopupCreateChatBinding? = null
    private val mBinding: PopupCreateChatBinding get() = _binding!!

    var mContinuation: Continuation<Int>? = null
    private var mType = -1

    init {
        width = 48.dp
        height = 164.dp
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable())
        animationStyle = R.style.Base_FadePopupAnimation
        _binding = PopupCreateChatBinding.inflate(LayoutInflater.from(context))
        contentView = mBinding.root
        initListener()
    }

    private fun initListener() {
        mBinding.createChatImvErnie.setOnClickListener {
            mType = 1
            dismiss()
        }

        mBinding.createChatImvSpark.setOnClickListener {
            mType = 2
            dismiss()
        }

        mBinding.createChatImvQwen.setOnClickListener {
            mType = 3
            dismiss()
        }

        setOnDismissListener {
            mContinuation?.resume(mType)
            mContinuation = null
        }
    }
}