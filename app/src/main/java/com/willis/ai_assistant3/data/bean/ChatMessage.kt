package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * description: 对话的一条详细信息
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
@Entity("chat_message")
class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("chat_info_id")
    val chatInfoId: Long,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("request")
    val request: Boolean,
    @ColumnInfo("create_millis")
    val createMillis: Long
)