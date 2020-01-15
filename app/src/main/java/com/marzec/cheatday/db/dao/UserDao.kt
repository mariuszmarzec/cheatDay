package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.marzec.cheatday.db.model.db.UserEntity
import io.reactivex.Single

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM users WHERE :email = email")
    fun getUser(email: String): Single<UserEntity>
}