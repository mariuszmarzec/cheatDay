package com.marzec.cheatday.db.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marzec.cheatday.db.model.db.UserEntity.Companion.COLUMN_EMAIL
import com.marzec.cheatday.db.model.db.UserEntity.Companion.COLUMN_ID

@Entity(
    tableName = UserEntity.NAME,
    indices = [Index(value = [COLUMN_EMAIL], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = COLUMN_EMAIL)
    val email: String
) {
    companion object {
        const val NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
    }
}