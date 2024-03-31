package com.willis.ai_assistant3.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.jiguang.verifysdk.api.JVerificationInterface
import cn.jiguang.verifysdk.api.LoginSettings
import com.willis.ai_assistant3.R
import com.willis.ai_assistant3.data.http.JVerification
import com.willis.ai_assistant3.repo.appRepo
import com.willis.base.data.BaseResult
import com.willis.base.services.httpService
import com.willis.base.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/13
 */
class LoginViewModel : ViewModel() {
    /**
     * 是否支持一键登录
     */
    fun canLoginAuth(): BaseResult<Unit> {
        if (!JVerificationInterface.isInitSuccess()) {
            return BaseResult.Failure(AppUtils.getString(R.string.login_init_failure))
        }
        if (!JVerificationInterface.checkVerifyEnable(AppUtils.appContext)) {
            return BaseResult.Failure(AppUtils.getString(R.string.login_verify_disable))
        }
        return BaseResult.Success(AppUtils.getString(R.string.login_verify_support), Unit)
    }

    /**
     * 是否支持短信登录
     */
    fun canSmsLoginAuth(): BaseResult<Unit> {
        return BaseResult.Failure(AppUtils.getString(R.string.login_sms_un_support))
    }

    /**
     * 一键登录
     */
    suspend fun loginAuth(context: Context): BaseResult<Unit> {
        return suspendCancellableCoroutine { continuation ->
            JVerificationInterface.loginAuth(context, LoginSettings()) { code, content, operator ->
                when (code) {
                    6000 -> {
                        viewModelScope.launch(Dispatchers.Default) {
                            val request = JVerification.createVerifyRequest(content)
                            val response = httpService.send(request)
                            continuation.resume(handleVerifyResult(JVerification.parseVerifyResponse(response)))
                        }
                    }

                    else -> {
                        val desc = "一键登录失败，code: $code, content: $content, operator: $operator"
                        continuation.resume(BaseResult.Failure(desc))
                    }
                }
            }
        }
    }

    /**
     * 短信登录
     */
    suspend fun smsLoginAuth(context: Context): BaseResult<Unit> {
        throw RuntimeException(AppUtils.getString(R.string.login_sms_un_support))
    }

    suspend fun adminLogin() = withContext(Dispatchers.Default) {
        appRepo.adminLogin()
    }

    private suspend fun handleVerifyResult(verifyResult: JVerification.ResponseBody): BaseResult<Unit> {
        return when (verifyResult) {
            is JVerification.ResponseBody.Success -> {
                appRepo.login(verifyResult.phone)
            }

            is JVerification.ResponseBody.Failure -> {
                BaseResult.Failure(verifyResult.toString())
            }
        }
    }
}