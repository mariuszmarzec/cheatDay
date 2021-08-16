package com.marzec.cheatday.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marzec.cheatday.db.converters.DateTimeConverter
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.migrations.MIGRATION_1_TO_2
import com.marzec.cheatday.db.migrations.MIGRATION_2_TO_3
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.db.model.db.WeightResultEntity

@Database(
    entities = [
        UserEntity::class,
        DayEntity::class,
        WeightResultEntity::class
    ],
    version = 3
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
                    MIGRATION_1_TO_2,
                    MIGRATION_2_TO_3
                ).build()
            }
            return db
        }
    }
}