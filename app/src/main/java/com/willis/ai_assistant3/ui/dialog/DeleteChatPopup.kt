package com.willis.ai_assistant3.ui.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.databinding.DialogDeleteChatBinding
import com.willis.base.ext.dp
import com.willis.base.ext.rectInWindow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2024/1/4
 */
class DeleteChatPopup(context: Context) : PopupWindow(context) {
    companion object {
        suspend fun showOnAnchor(context: Context, anchor: View): Boolean {
            return suspendCancellableCoroutine<Boolean> { continuation ->
                DeleteChatPopup(context).apply {
                    this.mContinuation = continuation
                }.showAtLocation(anchor, Gravity.TOP or Gravity.CENTER, 0, anchor.rectInWindow().top - 32.dp)
            }
        }
    }

    private var _binding: DialogDeleteChatBinding? = null
    private val mBinding: DialogDeleteChatBinding get() = _binding!!

    var mContinuation: Continuation<Boolean>? = null
    private var mIsDelete = false

    init {
        width = WRAP_CONTENT
        height = WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable())
        animationStyle = R.style.Base_FadePopupAnimation
        _binding = DialogDeleteChatBinding.inflate(LayoutInflater.from(context))
        contentView = mBinding.root
        initListener()
    }


    private fun initListener() {
        mBinding.deleteChatTv.setOnClickListener {
            mIsDelete = true
            dismiss()
        }

        setOnDismissListener {
            mContinuation?.resume(mIsDelete)
            mContinuation = null
        }
    }
}