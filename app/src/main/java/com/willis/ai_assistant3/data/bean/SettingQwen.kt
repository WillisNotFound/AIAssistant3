package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("setting_qwen")
data class SettingQwen(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("chat_info_id")
    val chatInfoId: Long,
    @ColumnInfo("api_key")
    val apiKey: String,
    @ColumnInfo("model")
    val model: String,
    @ColumnInfo("temperature")
    val temperature: Float
)