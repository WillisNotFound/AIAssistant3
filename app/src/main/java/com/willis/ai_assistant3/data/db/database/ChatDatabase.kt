package com.willis.ai_assistant3.data.db.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.ChatMessage
import com.willis.ai_assistant3.data.db.dao.ChatInfoDao
import com.willis.ai_assistant3.data.db.dao.ChatMessageDao
import com.willis.base.utils.AppUtils

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
@Database(entities = [ChatInfo::class, ChatMessage::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME_PREFIX = "chat_database"
        private var sInstance: ChatDatabase? = null
        private var sPhone: String? = null

        @Synchronized
        fun getInstance(phone: String): ChatDatabase {
            if (sPhone != phone) {
                sPhone = phone
                sInstance?.close()
                sInstance = null
            }
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    AppUtils.appContext,
                    ChatDatabase::class.java,
                    "$DATABASE_NAME_PREFIX-$sPhone"
                ).build()
            }
            return sInstance!!
        }
    }

    abstract fun chatInfoDao(): ChatInfoDao

    abstract fun chatMessageDao(): ChatMessageDao
}