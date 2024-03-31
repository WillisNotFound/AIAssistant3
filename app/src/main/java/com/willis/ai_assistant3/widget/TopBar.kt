package com.willis.ai_assistant3.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.databinding.WidgetTopBarBinding
import com.willis.base.ext.gone
import com.willis.base.ext.visible

/**
 * description: 顶部栏
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class TopBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var _binding: WidgetTopBarBinding? = null
    private val mBinding get() = _binding!!

    init {
        _binding = WidgetTopBarBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar)
        val title = typedArray.getString(R.styleable.TopBar_top_bar_title)
        val leftBtnSrc = typedArray.getResourceId(R.styleable.TopBar_left_btn_src, -1)
        val rightBtnSrc = typedArray.getResourceId(R.styleable.TopBar_right_btn_src, -1)
        typedArray.recycle()
        if (title != null) setTitle(title)
        if (leftBtnSrc != -1) setLeftBtnSrc(leftBtnSrc)
        if (rightBtnSrc != -1) setLeftBtnSrc(rightBtnSrc)
    }

    fun setTitle(title: String) {
        mBinding.topBarTvTitle.text = title
    }

    fun setLeftBtnSrc(srcId: Int) {
        mBinding.topBarImvLeft.setImageResource(srcId)
    }

    fun setRightBtnSrc(srcId: Int) {
        mBinding.topBarImvRight.setImageResource(srcId)
    }

    fun setLeftBtnOnClickListener(onClickListener: OnClickListener) {
        mBinding.topBarImvLeft.setOnClickListener(onClickListener)
    }

    fun setRightBtnOnClickListener(onClickListener: OnClickListener) {
        mBinding.topBarImvRight.setOnClickListener(onClickListener)
    }

    fun setLeftBtnVisible(visible: Boolean) {
        if (visible) mBinding.topBarImvLeft.visible() else mBinding.topBarImvLeft.gone()
    }

    fun setRightBtnVisible(visible: Boolean) {
        if (visible) mBinding.topBarImvRight.visible() else mBinding.topBarImvRight.gone()
    }
}