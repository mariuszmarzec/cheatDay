package com.marzec.cheatday.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.marzec.cheatday.db.model.db.WeightResultEntity

val MIGRATION_1_TO_2 = Migration1to2()

class Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `${WeightResultEntity.NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `value` REAL NOT NULL, `date` INTEGER NOT NULL, `user_id` TEXT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )")
    }
}
