package com.willis.ai_assistant3.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.databinding.WidgetRouterItemBinding
import com.willis.base.ext.gone
import com.willis.base.ext.visible

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/17
 */
class RouterItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        const val BG_TYPE_CENTER = 0
        const val BG_TYPE_TOP = 1
        const val BG_TYPE_BOTTOM = 2
        const val BG_TYPE_AROUND = 3
    }


    private var _binding: WidgetRouterItemBinding? = null
    private val mBinding get() = _binding!!

    init {
        _binding = WidgetRouterItemBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RouterItem)
        val title = typedArray.getString(R.styleable.RouterItem_router_item_title)
        val subtitle = typedArray.getString(R.styleable.RouterItem_router_item_subtitle)
        val startImvSrc = typedArray.getResourceId(R.styleable.RouterItem_start_imv_src, -1)
        val endImvSrc = typedArray.getResourceId(R.styleable.RouterItem_end_imv_src, -1)
        val backgroundType = typedArray.getInt(R.styleable.RouterItem_background_type, -1)
        val backgroundTint = typedArray.getColor(R.styleable.RouterItem_background_tint, Color.WHITE)
        typedArray.recycle()
        if (title != null) setTitle(title)
        setSubtitle(subtitle)
        setStartImvSrc(startImvSrc)
        setEndImvSrc(endImvSrc)
        setBackground(backgroundType)
        setBackgroundTine(backgroundTint)
    }

    fun setTitle(title: String) {
        mBinding.routerTvTitle.text = title
    }

    fun setSubtitle(subtitle: String?) {
        if (subtitle == null) {
            mBinding.routerTvSubtitle.gone()
        } else {
            mBinding.routerTvSubtitle.visible()
            mBinding.routerTvSubtitle.text = subtitle
        }
    }

    fun setStartImvSrc(src: Int) {
        if (src == -1) {
            mBinding.routerImvStart.gone()
        } else {
            mBinding.routerImvStart.visible()
            mBinding.routerImvStart.setImageResource(src)
        }
    }

    fun setEndImvSrc(src: Int) {
        if (src == -1) {
            mBinding.routerImvEnd.gone()
        } else {
            mBinding.routerImvEnd.visible()
            mBinding.routerImvEnd.setImageResource(src)
        }
    }

    fun setBackground(backgroundType: Int) {
        when (backgroundType) {
            BG_TYPE_CENTER -> setBackground(ColorDrawable(Color.WHITE))
            BG_TYPE_TOP -> setBackgroundResource(R.drawable.shape_bg_round_top)
            BG_TYPE_BOTTOM -> setBackgroundResource(R.drawable.shape_bg_round_bottom)
            BG_TYPE_AROUND -> setBackgroundResource(R.drawable.shape_bg_round)
        }
    }

    fun setBackgroundTine(backgroundTint: Int) {
        background?.setTint(backgroundTint)
    }
}