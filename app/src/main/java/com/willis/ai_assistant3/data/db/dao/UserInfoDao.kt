package com.willis.ai_assistant3.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.willis.ai_assistant3.data.bean.UpdateNickname
import com.willis.ai_assistant3.data.bean.UserInfo

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/14
 */
@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(userInfo: UserInfo)

    @Update
    suspend fun update(userInfo: UserInfo)

    @Delete
    suspend fun delete(userInfo: UserInfo)

    @Query("SELECT * FROM user_info WHERE phone = :phone")
    suspend fun queryByPhone(phone: String): UserInfo?

    /**
     * 查询所有用户，根据最后登录时间倒序排序
     */
    @Query("SELECT * FROM user_info ORDER BY last_login_millis DESC")
    suspend fun queryAll(): List<UserInfo>

    @Update(entity = UserInfo::class)
    suspend fun updateNickname(updateNickname: UpdateNickname): Int
}