package com.willis.ai_assistant3.ui.dialog

import android.app.Dialog
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.FragmentManager
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseDialog
import com.willis.ai_assistant3.databinding.DialogDeleteChatBinding
import com.willis.base.ext.displayMetrics
import com.willis.base.ext.dp
import com.willis.base.ext.rectInWindow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/26
 */
@Deprecated("展示的坐标有误", ReplaceWith("DeleteChatPopup"))
class DeleteChatDialog : BaseDialog<DialogDeleteChatBinding>() {
    companion object {
        suspend fun show(view: View, fm: FragmentManager): Boolean {
            return suspendCancellableCoroutine { continuation ->
                DeleteChatDialog().apply {
                    this.mContinuation = continuation
                    this.mRect = view.rectInWindow()
                }.show(fm, toString())
            }
        }
    }

    var mContinuation: Continuation<Boolean>? = null
    var mRect: Rect? = null

    private var mIsDelete = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                decorView.setPadding(0, 0, 0, 0)
                attributes = attributes.apply {
                    width = WRAP_CONTENT
                    height = WRAP_CONTENT
                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    windowAnimations = R.style.Base_FadeDialogAnimation
                    dimAmount = 0F
                }
                setBackgroundDrawable(ColorDrawable())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            attributes = attributes.apply {
                y = (mRect?.top ?: (displayMetrics.heightPixels / 2)) - 32.dp
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mContinuation?.resume(mIsDelete)
        mContinuation = null
    }

    override fun inflateBinding(layoutInflater: LayoutInflater, container: ViewGroup?): DialogDeleteChatBinding {
        return DialogDeleteChatBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {

    }

    override fun initListener() {
        mBinding.deleteChatTv.setOnClickListener {
            mIsDelete = true
            dismissAllowingStateLoss()
        }
    }
}