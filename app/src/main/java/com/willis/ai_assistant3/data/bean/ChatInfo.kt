package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * description: 整个对话的基本信息
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
@Entity("chat_info")
class ChatInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("type")
    val type: Int, // 0 - 文心一言，1 - 讯飞星火，2 - 阿里千问
    @ColumnInfo("nickname")
    val nickname: String,
    @ColumnInfo("create_millis")
    val createMillis: Long,
    @ColumnInfo("last_chat_millis")
    var lastChatMillis: Long,
    @ColumnInfo("last_chat_message")
    var lastChatMessage: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatInfo

        if (id != other.id) return false
        if (type != other.type) return false
        if (nickname != other.nickname) return false
        if (createMillis != other.createMillis) return false
        if (lastChatMillis != other.lastChatMillis) return false
        if (lastChatMessage != other.lastChatMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type
        result = 31 * result + nickname.hashCode()
        result = 31 * result + createMillis.hashCode()
        result = 31 * result + lastChatMillis.hashCode()
        result = 31 * result + lastChatMessage.hashCode()
        return result
    }
}