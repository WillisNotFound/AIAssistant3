package com.willis.ai_assistant3.data.db.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.willis.ai_assistant3.data.bean.ChatInfo
import com.willis.ai_assistant3.data.bean.ChatMessage
import com.willis.ai_assistant3.data.bean.SettingErnie
import com.willis.ai_assistant3.data.bean.SettingQwen
import com.willis.ai_assistant3.data.bean.SettingSpark
import com.willis.ai_assistant3.data.db.dao.ChatInfoDao
import com.willis.ai_assistant3.data.db.dao.ChatMessageDao
import com.willis.ai_assistant3.data.db.dao.SettingErnieDao
import com.willis.ai_assistant3.data.db.dao.SettingQwenDao
import com.willis.ai_assistant3.data.db.dao.SettingSparkDao
import com.willis.base.utils.AppUtils

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
@Database(
    entities = [
        ChatInfo::class,
        ChatMessage::class,
        SettingErnie::class,
        SettingQwen::class,
        SettingSpark::class
    ],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME_PREFIX = "chat_database"
        private var sInstance: UserDatabase? = null
        private var sPhone: String? = null

        @Synchronized
        fun getInstance(phone: String): UserDatabase {
            if (sPhone != phone) {
                sPhone = phone
                sInstance?.close()
                sInstance = null
            }
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                    AppUtils.appContext,
                    UserDatabase::class.java,
                    "$DATABASE_NAME_PREFIX-$sPhone"
                ).build()
            }
            return sInstance!!
        }
    }

    abstract fun chatInfoDao(): ChatInfoDao

    abstract fun chatMessageDao(): ChatMessageDao

    abstract fun settingErnieDao(): SettingErnieDao

    abstract fun settingQwenDao(): SettingQwenDao

    abstract fun settingSparkDao(): SettingSparkDao
}