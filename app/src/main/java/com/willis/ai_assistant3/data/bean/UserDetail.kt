package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * description: 用户详细信息
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
@Entity("user_detail")
class UserDetail(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    // ==========文心一言相关=========
    @ColumnInfo("ernie_client_id")
    val ernieClientId: String,
    @ColumnInfo("ernie_client_secret")
    val ernieClientSecret: String,
    @ColumnInfo("ernie_access_token")
    val ernieAccessToken: String,
    @ColumnInfo("ernie_url")
    val ernieUrl: String,
    @ColumnInfo("ernie_temperature")
    val ernieTemperature: Float,
    // ==========讯飞星火相关==========
    @ColumnInfo("spark_app_id")
    val sparkAppId: String,
    @ColumnInfo("spark_api_key")
    val sparkApiKey: String,
    @ColumnInfo("spark_api_secret")
    val sparkApiSecret: String,
    @ColumnInfo("spark_temperature")
    val sparkTemperature: Float,
    // ==========阿里千问相关==========
    @ColumnInfo("qwen_api_key")
    val qwenApiKey: String,
    @ColumnInfo("qwen_model")
    val qwenModel: String,
    @ColumnInfo("qwen_temperature")
    val qwenTemperature: Float
)

@Entity
class UpdateErnieClientId(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("ernie_client_id")
    val ernieClientId: String
)

@Entity
class UpdateErnieClientSecret(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("ernie_client_secret")
    val ernieClientSecret: String
)

@Entity
class UpdateErnieAccessToken(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("ernie_access_token")
    val ernieAccessToken: String
)

@Entity
class UpdateErnieUrl(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("ernie_url")
    val ernieUrl: String
)

@Entity
class UpdateErnieTemperature(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("ernie_temperature")
    val ernieTemperature: Float
)

@Entity
class UpdateSparkAppId(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("spark_app_id")
    val sparkAppId: String
)

@Entity
class UpdateSparkApiKey(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("spark_api_key")
    val sparkApiKey: String
)

@Entity
class UpdateSparkApiSecret(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("spark_api_secret")
    val sparkApiSecret: String
)

@Entity
class UpdateSparkTemperature(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("spark_temperature")
    val sparkTemperature: Float
)

@Entity
class UpdateQwenApiKey(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("qwen_api_key")
    val qwenApiKey: String
)

@Entity
class UpdateQwenModel(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("qwen_model")
    val qwenModel: String
)

@Entity
class UpdateQwenTemperature(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("qwen_temperature")
    val qwenTemperature: Float
)