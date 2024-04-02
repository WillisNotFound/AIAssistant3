package com.willis.ai_assistant3.ui.chat

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.willis.ai_assistant3.base.BaseActivity
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.databinding.ActivityChatBinding
import com.willis.ai_assistant3.ui.adapter.ChatMessageAdapter
import com.willis.ai_assistant3.ui.setting.ernie.BotERNIESettingActivity
import com.willis.ai_assistant3.ui.setting.qwen.BotQwenSettingActivity
import com.willis.ai_assistant3.ui.setting.spark.BotSparkSettingActivity
import com.willis.base.ext.addOnTextChangeListener
import com.willis.base.ext.collectWhenResumed
import com.willis.base.ext.gone
import com.willis.base.ext.visible
import com.willis.base.utils.KeyboardUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class ChatActivity : BaseActivity<ActivityChatBinding>(), OnGlobalLayoutListener {
    companion object {
        private const val EXTRA_CHAT_INFO_ID = "chat_info_id"
        private const val EXTRA_CHAT_INFO_TYPE = "chat_info_type"

        fun startAction(context: Context, chatInfo: ChatInfo) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(EXTRA_CHAT_INFO_ID, chatInfo.id)
            intent.putExtra(EXTRA_CHAT_INFO_TYPE, chatInfo.type)
            context.startActivity(intent)
        }
    }

    override val isLightStatusBar: Boolean = true

    private val mViewModel by viewModels<ChatViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0)) as T
            }
        }
    })
    private val mContentFlow = MutableStateFlow<String?>(null)
    private lateinit var mAdapter: ChatMessageAdapter
    private var mDecorViewVisibleHeight: Int = 0

    override fun onGlobalLayout() {
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val height = rect.height()
        if (mDecorViewVisibleHeight == 0) {
            mDecorViewVisibleHeight = height
            return
        }
        if (mDecorViewVisibleHeight == height) {
            return
        }
        if (mDecorViewVisibleHeight - height > 200) { // 根视图显示高度变小超过200，可以看作软键盘显示了
            scrollToBottom()
            mDecorViewVisibleHeight = height
            return
        }
        if (height - mDecorViewVisibleHeight > 200) { // 根视图显示高度变大超过200，可以看作软键盘隐藏了
            mDecorViewVisibleHeight = height
            return
        }
    }

    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivityChatBinding.inflate(layoutInflater)

    override fun initView() {
        mBinding.chatTopBar.apply {
            mViewModel.titleFlow.collectWhenResumed(lifecycleScope) {
                setTitle(it)
            }
        }

        mAdapter = ChatMessageAdapter(intent.getIntExtra(EXTRA_CHAT_INFO_TYPE, 0)).apply {
            mViewModel.newChatMessageFlow.collectWhenResumed(lifecycleScope) {
                it?.let {
                    appendData(it)
                    notifyItemInserted(itemCount - 1)
                    mBinding.chatTvEmpty.gone()
                    scrollToBottom()
                }
            }

            lifecycleScope.launchWhenResumed {
                val list = mViewModel.refresh()
                refreshData(list)
                notifyDataSetChanged()
                if (list.isEmpty()) {
                    mBinding.chatTvEmpty.visible()
                } else {
                    mBinding.chatTvEmpty.gone()
                    scrollToBottom()
                }
            }
        }

        mBinding.chatRecyclerview.apply {
            adapter = mAdapter
        }

        mBinding.chatBtnSend.apply {
            combine(mContentFlow, mViewModel.loadingFlow) { content, loading ->
                !content.isNullOrEmpty() && !loading
            }.collectWhenResumed(lifecycleScope) { clickable ->
                if (clickable) {
                    alpha = 1.0F
                    isClickable = true
                } else {
                    alpha = 0.6F
                    isClickable = false
                }
            }
        }

    }

    override fun initListener() {
        mBinding.chatTopBar.setLeftBtnOnClickListener {
            finish()
        }

        mBinding.chatTopBar.setRightBtnOnClickListener {
            when(intent.getIntExtra(EXTRA_CHAT_INFO_TYPE, -1)) {
                0 -> {
                    BotERNIESettingActivity.startAction(
                        this,
                        intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0),
                        BotERNIESettingActivity.ENTER_FROM_CHAT
                    )
                }

                1 -> {
                    BotSparkSettingActivity.startAction(
                        this,
                        intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0),
                        BotSparkSettingActivity.ENTER_FROM_CHAT
                    )
                }

                2 -> {
                    BotQwenSettingActivity.startAction(
                        this,
                        intent.getLongExtra(EXTRA_CHAT_INFO_ID, 0),
                        BotQwenSettingActivity.ENTER_FROM_CHAT
                    )
                }
            }
        }

        mBinding.chatRecyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN) {
                    KeyboardUtils.hideKeyboard()
                }
                return super.onInterceptTouchEvent(rv, e)
            }
        })

        mBinding.chatEdtMessage.addOnTextChangeListener {
            mContentFlow.value = it?.toString()
        }

        mBinding.chatBtnSend.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val content = mBinding.chatEdtMessage.text.toString()
                mBinding.chatEdtMessage.text = null
                mViewModel.send(content)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.root.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    private fun scrollToBottom() {
        if (mAdapter.itemCount > 1) {
            mBinding.chatRecyclerview.scrollToPosition(mAdapter.itemCount - 1)
        }
    }
}