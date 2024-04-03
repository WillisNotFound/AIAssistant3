package com.willis.ai_assistant3.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.willis.ai_assistant3.data.bean.ChatMessage

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/26
 */
@Dao
interface ChatMessageDao {
    /**
     * 返回主键id
     */
    @Insert
    suspend fun insert(chatMessage: ChatMessage): Long

    /**
     * 返回成功删除的行数
     */
    @Query("DELETE FROM chat_message WHERE id = :id")
    suspend fun delete(id: Long): Int

    /**
     * 返回成功删除的行数
     */
    @Query("DELETE FROM chat_message WHERE chat_info_id = :chatInfoId")
    suspend fun deleteByChatInfoId(chatInfoId: Long): Int

    /**
     * 从下往上查询，返回的列表为倒序
     */
    @Query("SELECT * FROM chat_message WHERE chat_info_id = :chatInfoId ORDER BY id DESC LIMIT :size OFFSET :startIndex")
    suspend fun queryByChatInfoId(chatInfoId: Long, startIndex: Int, size: Int): List<ChatMessage>

    /**
     * 从下往上查询，返回的列表为倒序
     */
    @Query("SELECT * FROM chat_message WHERE chat_info_id = :chatInfoId ORDER BY id DESC")
    suspend fun queryByChatInfoIdDesc(chatInfoId: Long): List<ChatMessage>

    /**
     * 从上往下查询，返回的列表为正序
     */
    @Query("SELECT * FROM chat_message WHERE chat_info_id = :chatInfoId ORDER BY id")
    suspend fun queryByChatInfoId(chatInfoId: Long): List<ChatMessage>

    /**
     * 返回成功更新的行数
     */
    @Update
    suspend fun update(chatMessage: ChatMessage): Int
}