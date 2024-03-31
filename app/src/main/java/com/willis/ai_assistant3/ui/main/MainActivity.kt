package com.willis.ai_assistant3.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.data.db.database.AppDatabase
import com.willis.ai_assistant3.databinding.ActivityMainBinding
import com.willis.ai_assistant3.repo.appRepo
import com.willis.ai_assistant3.ui.adapter.MainPagerAdapter
import com.willis.base.utils.AppUtils
import com.willis.spark.Spark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description: 主页面
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/12
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val isLightStatusBar = true

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                val phone = appRepo.currentPhoneFlow.value!!
                AppDatabase.instance.userDetailDao().queryByPhone(phone)?.let {
                    Spark.initSDK(
                        AppUtils.appContext,
                        it.sparkAppId,
                        it.sparkApiKey,
                        it.sparkApiSecret,
                        it.sparkTemperature
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Spark.unInitSDK()
    }

    override fun initView() {
        mBinding.mainViewPager.apply {
            adapter = MainPagerAdapter(this@MainActivity)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    mBinding.mainBottomNav.selectedItemId = mBinding.mainBottomNav.menu.getItem(position).itemId
                    updateTopBar(position)
                }
            })
        }
    }

    override fun initListener() {
        mBinding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_chat -> {
                    mBinding.mainViewPager.currentItem = 0
                }

                R.id.navigation_mine -> {
                    mBinding.mainViewPager.currentItem = 1
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (mBinding.mainViewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mBinding.mainViewPager.currentItem = 0
        }
    }

    private fun updateTopBar(position: Int) {
        when (position) {
            0 -> {
                mBinding.mainTopBar.setTitle(AppUtils.getString(R.string.chat_history_title))
            }

            1 -> {
                mBinding.mainTopBar.setTitle(AppUtils.getString(R.string.mine_title))
            }
        }
    }
}