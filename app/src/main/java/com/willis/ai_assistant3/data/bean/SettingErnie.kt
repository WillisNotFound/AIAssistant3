package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("setting_ernie")
data class SettingErnie (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("chat_info_id")
    val chatInfoId: Long,
    @ColumnInfo("client_id")
    val clientId: String,
    @ColumnInfo("client_secret")
    val clientSecret: String,
    @ColumnInfo("access_token")
    val accessToken: String,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("temperature")
    val temperature: Float,
    @ColumnInfo("context_times")
    val contextTimes: Int
)