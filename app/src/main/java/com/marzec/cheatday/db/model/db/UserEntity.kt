package com.marzec.cheatday.db.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
    @ColumnInfo(name = COLUMN_EMAIL)
    val email: String
) {
    companion object {
        const val NAME = "users"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_EMAIL = "email"
    }
}