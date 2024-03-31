package com.willis.ai_assistant3.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.willis.ai_assistant3.data.bean.ChatInfo

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/19
 */
@Dao
interface ChatInfoDao {
    /**
     * 返回主键
     */
    @Insert
    suspend fun insert(chatInfo: ChatInfo): Long

    @Delete
    suspend fun delete(chatInfo: ChatInfo)

    @Query("DELETE FROM chat_info WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(chatInfo: ChatInfo)

    @Query("SELECT * FROM chat_info WHERE id = :id")
    suspend fun queryById(id: Long): ChatInfo?

    /**
     * 查询所有对话，根据最后对话时间倒序排序
     */
    @Query("SELECT * FROM chat_info ORDER BY last_chat_millis DESC")
    suspend fun queryAll(): List<ChatInfo>
}