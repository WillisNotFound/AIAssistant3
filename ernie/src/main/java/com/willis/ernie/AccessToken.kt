package com.willis.ernie

import com.google.gson.annotations.SerializedName

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/28
 */
internal sealed interface AccessToken {
    data class Success(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("expires_in")
        val expiresIn: Int
    ) : AccessToken

    data class Failure(
        @SerializedName("error")
        var error: String,
        @SerializedName("error_description")
        var errorDescription: String
    ) : AccessToken
}