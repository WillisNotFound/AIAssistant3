package com.willis.ai_assistant3.data.http

import android.util.Base64
import com.google.gson.annotations.SerializedName
import com.willis.base.utils.AppUtils
import com.willis.base.utils.GsonUtils
import okhttp3.Credentials
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher


/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/13
 */
object JVerification {
    /**
     * 极光公钥
     */
    private const val RSA_KEY_PUBLIC =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHH3zbN9zd4SqF31cwCC+RY7TyeZ2+i9XUvKW0AC3vnYUWu3aLKVQvsHjblP8C/lStC8G0ioJPjt4MvV4/uOOInZgAebRV+fKynObdcEzTT1VOPn8rKV9yVXFPkQl1cIFfhl4ZzSEosWX3yB1tvAJGGoShj7euPsHxBujs3LborQIDAQAB"

    /**
     * 极光私钥
     */
    private const val RSA_KEY_PRIVATE =
        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMcffNs33N3hKoXfVzAIL5FjtPJ5nb6L1dS8pbQALe+dhRa7dospVC+weNuU/wL+VK0LwbSKgk+O3gy9Xj+444idmAB5tFX58rKc5t1wTNNPVU4+fyspX3JVcU+RCXVwgV+GXhnNISixZffIHW28AkYahKGPt64+wfEG6OzctuitAgMBAAECgYBWWvuE/tcUisAsSs2/EO3AOeORfNjCeFaF6kJaq/FAB6peT3J7q7LM89WWNSnbHH2Qhpa9O4yEAEugbsffDsnuA7u2i64SOgp6x81Z4C6r8eTLlSsKrSCddfHPD3P5QuEZfpCz/YDvaywjwBu2mH8nyBCAG2N8W3C9VVNKzfQxQQJBAOZCHxOwlDNY1Kp3vp6vFY1HVVTrpXwwMD2sEkempX+rRwp2QEKNnH3b6H8oZb6e5Wq+cZZe/wfs7gJ8S1CHWDUCQQDdYkostLYenLSxIaBKimktGzxJMKlcc4bZGwg0lpjBa24bzE0QiiwFwErNPYg2jK6IdvD6s38SZb/AakCDBo2ZAkB1rn+hmZL090ah5Vd4VLWoIjgbkeBfK2XMb2BcXHCjYo+JakyHd0bvEHCwZJ4zoZRRTQpStzSTAWXXwVzgyHutAkEAm+NOX9SQOVYXVnNbL6HvFbgL523AlD01Q0Npr74rqmc/jYt66J6jCRmykns1o1PEwosefSpP3GCReahX7Ot8YQJBAIoLX3kmfD5lvQA0NmyUtzMlKUf8a545JsHSfkw42droonkPEwAKOZXC5SUiy2aBDs1hjcouVgwAq+uqcQ2UYUw="

    /**
     * URL，提交 loginToken，验证后返回加密的手机号码
     */
    private const val URL_VERIFICATION = "https://api.verification.jpush.cn/v1/web/loginTokenVerify"

    private const val APP_KEY = "dcdb3fa736ef1faae64a1b56"

    private const val MASTER_SECRET = "c1910e77ed6f5bd7154e1ea6"

    fun createVerifyRequest(loginToken: String): Request {
        val requestBody = GsonUtils.toJson(RequestBody(loginToken)).toRequestBody()
        return Request.Builder()
            .url(URL_VERIFICATION)
            .method("POST", requestBody)
            .header("Content-Type", "application/json")
            .header("Authorization", Credentials.basic(APP_KEY, MASTER_SECRET))
            .build()
    }

    fun parseVerifyResponse(response: Response): ResponseBody {
        val body = response.body?.string() ?: ""
        val jsonObject = GsonUtils.fromJson(body)
        return when (jsonObject.get("code")?.asInt) {
            8000 -> {
                GsonUtils.fromJson(body, ResponseBody.Success::class.java).decryptPhone()
            }

            null -> {
                ResponseBody.Failure(response.code, response.message)
            }

            else -> {
                ResponseBody.Failure(response.code, response.message)
            }
        }
    }

    @Throws(Exception::class)
    fun ResponseBody.Success.decryptPhone(): ResponseBody.Success {
        val encodedKey = Base64.decode(RSA_KEY_PRIVATE, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(encodedKey)
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val phonePlainBytes: ByteArray = Base64.decode(phone, Base64.DEFAULT)
        val phonePlainText = String(cipher.doFinal(phonePlainBytes))
        return ResponseBody.Success(id, code, content, exID, phonePlainText)
    }

    class RequestBody(
        @SerializedName("loginToken")
        val loginToken: String,
        @SerializedName("exID")
        val exID: String = AppUtils.appContext.applicationInfo.packageName
    )

    sealed interface ResponseBody {
        class Success(
            @SerializedName("id")
            val id: Long,
            @SerializedName("code")
            val code: Int,
            @SerializedName("content")
            val content: String,
            @SerializedName("exID")
            val exID: String?,
            @SerializedName("phone")
            val phone: String
        ) : ResponseBody {
            override fun toString(): String {
                return "Success(id=$id, code=$code, content='$content', exID=$exID, phone='$phone')"
            }
        }

        class Failure(
            @SerializedName("code")
            val code: Int,
            @SerializedName("content")
            val content: String
        ) : ResponseBody {
            override fun toString(): String {
                return "Failure(code=$code, content='$content')"
            }
        }
    }
}