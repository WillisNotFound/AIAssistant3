package com.willis.ai_assistant3.data.db.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.willis.ai_assistant3.data.bean.UserDetail
import com.willis.ai_assistant3.data.bean.UserInfo
import com.willis.ai_assistant3.data.db.dao.UserDetailDao
import com.willis.ai_assistant3.data.db.dao.UserInfoDao
import com.willis.base.utils.AppUtils.appContext

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/14
 */
@Database(entities = [UserInfo::class, UserDetail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "app_database"
        private lateinit var INSTANCE: AppDatabase

        val instance: AppDatabase
            get() {
                if (!this::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        appContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
                return INSTANCE
            }
    }

    abstract fun userInfoDao(): UserInfoDao

    abstract fun userDetailDao(): UserDetailDao
}