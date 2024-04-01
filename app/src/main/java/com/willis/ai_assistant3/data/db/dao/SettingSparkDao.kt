package com.willis.ai_assistant3.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willis.ai_assistant3.data.bean.SettingSpark

@Dao
interface SettingSparkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(settingSpark: SettingSpark)

    /**
     * 返回成功删除的行数
     */
    @Query("DELETE FROM setting_qwen WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    /**
     * 返回成功删除的行数
     */
    @Query("DELETE FROM setting_spark WHERE chat_info_id = :chatInfoId")
    suspend fun deleteByChatInfoId(chatInfoId: Long): Int

    @Query("SELECT * FROM setting_spark WHERE id = :id")
    suspend fun queryById(id: Long): SettingSpark?

    @Query("SELECT * FROM setting_spark WHERE chat_info_id = :chatInfoId")
    suspend fun queryByChatInfoId(chatInfoId: Long): SettingSpark?
}