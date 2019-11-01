package com.marzec.cheatday.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}