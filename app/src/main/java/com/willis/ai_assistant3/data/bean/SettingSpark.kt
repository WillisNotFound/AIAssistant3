package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("setting_spark")
data class SettingSpark (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("chat_info_id")
    val chatInfoId: Long,
    @ColumnInfo("app_id")
    val appId: String,
    @ColumnInfo("api_key")
    val apiKey: String,
    @ColumnInfo("api_secret")
    val apiSecret: String,
    @ColumnInfo("temperature")
    val temperature: Float,
)