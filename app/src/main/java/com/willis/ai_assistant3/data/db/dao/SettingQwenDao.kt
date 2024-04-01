package com.willis.ai_assistant3.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willis.ai_assistant3.data.bean.SettingQwen

@Dao
interface SettingQwenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(settingQwen: SettingQwen)

    /**
     * 返回成功删除的行数
     */
    @Query("DELETE FROM setting_qwen WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    /**
     * 返回成功删除的行数
     */
    @Query("DELETE FROM setting_qwen WHERE chat_info_id = :chatInfoId")
    suspend fun deleteByChatInfoId(chatInfoId: Long): Int

    @Query("SELECT * FROM setting_qwen WHERE id = :id")
    suspend fun queryById(id: Long): SettingQwen?

    @Query("SELECT * FROM setting_qwen WHERE chat_info_id = :chatInfoId")
    suspend fun queryByChatInfoId(chatInfoId: Long): SettingQwen?
}