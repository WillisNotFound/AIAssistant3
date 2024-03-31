package com.willis.ai_assistant3.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willis.ai_assistant3.repo.appRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/12
 */
class SplashViewModel : ViewModel() {
    companion object {
        private const val DEFAULT_COUNT_DOWN = 3
    }

    private val mCountDownFlow = MutableStateFlow(DEFAULT_COUNT_DOWN)
    val countDownFlow: StateFlow<Int> = mCountDownFlow

    private var mCountDownJob: Job? = null

    fun startCountDown(onFinish: () -> Unit) {
        mCountDownJob?.cancel()
        mCountDownJob = viewModelScope.launch {
            var current = DEFAULT_COUNT_DOWN
            repeat(DEFAULT_COUNT_DOWN + 1) {
                mCountDownFlow.value = current--
                delay(1000)
            }
            onFinish()
        }
    }

    fun stopCountDown(onStop: () -> Unit) {
        mCountDownJob?.cancel()
        mCountDownJob = null
        onStop()
    }

    suspend fun checkLogin(): Boolean {
        return withContext(Dispatchers.Default) {
            return@withContext appRepo.isLogin()
        }
    }
}