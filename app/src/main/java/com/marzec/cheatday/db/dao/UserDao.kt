package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.marzec.cheatday.db.model.db.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM users WHERE :email = email")
    fun observeUser(email: String): Flow<UserEntity>

    @Query("SELECT * FROM users WHERE :email = email")
    suspend fun getUser(email: String): UserEntity
}