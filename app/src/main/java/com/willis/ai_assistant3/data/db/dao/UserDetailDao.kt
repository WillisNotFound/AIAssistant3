package com.willis.ai_assistant3.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.willis.ai_assistant3.data.bean.UpdateErnieAccessToken
import com.willis.ai_assistant3.data.bean.UpdateSparkApiKey
import com.willis.ai_assistant3.data.bean.UpdateSparkApiSecret
import com.willis.ai_assistant3.data.bean.UpdateSparkAppId
import com.willis.ai_assistant3.data.bean.UpdateErnieClientId
import com.willis.ai_assistant3.data.bean.UpdateErnieClientSecret
import com.willis.ai_assistant3.data.bean.UpdateErnieTemperature
import com.willis.ai_assistant3.data.bean.UpdateErnieUrl
import com.willis.ai_assistant3.data.bean.UpdateQwenApiKey
import com.willis.ai_assistant3.data.bean.UpdateQwenModel
import com.willis.ai_assistant3.data.bean.UpdateQwenTemperature
import com.willis.ai_assistant3.data.bean.UpdateSparkTemperature
import com.willis.ai_assistant3.data.bean.UserDetail

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
@Dao
interface UserDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(userDetail: UserDetail)

    @Delete
    suspend fun delete(userDetail: UserDetail)

    @Query("SELECT * FROM user_detail WHERE phone = :phone")
    suspend fun queryByPhone(phone: String): UserDetail?

    @Update(entity = UserDetail::class)
    suspend fun updateErnieClientId(updateErnieClientId: UpdateErnieClientId): Int

    @Update(entity = UserDetail::class)
    suspend fun updateErnieClientSecret(updateErnieClientSecret: UpdateErnieClientSecret): Int

    @Update(entity = UserDetail::class)
    suspend fun updateErnieAccessToken(updateErnieAccessToken: UpdateErnieAccessToken): Int

    @Update(entity = UserDetail::class)
    suspend fun updateErnieUrl(updateErnieUrl: UpdateErnieUrl): Int

    @Update(entity = UserDetail::class)
    suspend fun updateErnieTemperature(updateErnieTemperature: UpdateErnieTemperature): Int

    @Update(entity = UserDetail::class)
    suspend fun updateSparkAppId(updateSparkAppId: UpdateSparkAppId): Int

    @Update(entity = UserDetail::class)
    suspend fun updateSparkApiKey(updateSparkApiKey: UpdateSparkApiKey): Int

    @Update(entity = UserDetail::class)
    suspend fun updateSparkApiSecret(updateSparkApiSecret: UpdateSparkApiSecret): Int

    @Update(entity = UserDetail::class)
    suspend fun updateSparkTemperature(updateSparkTemperature: UpdateSparkTemperature): Int

    @Update(entity = UserDetail::class)
    suspend fun updateQwenApiKey(updateQwenApiKey: UpdateQwenApiKey): Int

    @Update(entity = UserDetail::class)
    suspend fun updateQwenModel(updateQwenModel: UpdateQwenModel): Int

    @Update(entity = UserDetail::class)
    suspend fun updateQwenTemperature(updateQwenTemperature: UpdateQwenTemperature): Int
}