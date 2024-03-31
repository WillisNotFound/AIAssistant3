package com.willis.ai_assistant3.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * description: 用户基本信息
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/14
 */
@Entity("user_info")
class UserInfo(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("nickname")
    var nickname: String,
    @ColumnInfo("create_millis")
    val createMillis: Long,
    @ColumnInfo("last_login_millis")
    val lastLoginMillis: Long
)

@Entity
class UpdateNickname(
    @PrimaryKey
    @ColumnInfo("phone")
    val phone: String,
    @ColumnInfo("nickname")
    val nickname: String
)