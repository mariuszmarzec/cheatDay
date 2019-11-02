package com.marzec.cheatday.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.UserEntity
import java.util.*

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {

        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (!::db.isInitialized) {
                db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        val email = "mariusz.marzec00@gmail.com"
                        val searchQuery = SupportSQLiteQueryBuilder.builder(UserEntity.NAME)
                            .selection("email = ?", arrayOf(email))
                            .create()
                        if (db.query(searchQuery).count == 0) {
                            db.insert(
                                UserEntity.NAME,
                                SQLiteDatabase.CONFLICT_FAIL,
                                ContentValues().also {
                                    it.put(UserEntity.COLUMN_UUID, UUID.randomUUID().toString())
                                    it.put(UserEntity.COLUMN_EMAIL, email)
                                }
                            )
                        }
                    }
                }).build()
            }
            return db
        }
    }
}