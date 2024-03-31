package com.willis.ai_assistant3.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/26
 */
abstract class BaseDialog<VB : ViewBinding>: DialogFragment() {
    protected lateinit var mBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = inflateBinding(inflater, container)
        initView()
        initListener()
        return mBinding.root
    }

    protected abstract fun inflateBinding(layoutInflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun initView()

    protected abstract fun initListener()
}