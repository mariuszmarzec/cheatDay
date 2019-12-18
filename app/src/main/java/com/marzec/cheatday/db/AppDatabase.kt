package com.marzec.cheatday.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.marzec.cheatday.db.converters.DateTimeConverter
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.migrations.MIGRATION_1_TO_2
import com.marzec.cheatday.db.model.DayEntity
import com.marzec.cheatday.db.model.UserEntity
import com.marzec.cheatday.db.model.WeightResultEntity
import java.util.*

@Database(
    entities = [
        UserEntity::class,
        DayEntity::class,
        WeightResultEntity::class
    ],
    version = 2
)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getDayDao(): DayDao

    abstract fun getWeightDao(): WeightDao

    companion object {

        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (!::db.isInitialized) {
                db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database"
                ).addMigrations(
                    MIGRATION_1_TO_2
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