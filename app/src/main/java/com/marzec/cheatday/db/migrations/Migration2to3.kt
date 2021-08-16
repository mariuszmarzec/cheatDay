package com.marzec.cheatday.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.db.model.db.WeightResultEntity

val MIGRATION_2_TO_3 = Migration2to3()

class Migration2to3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `${WeightResultEntity.NAME}`")
        database.execSQL("DROP TABLE `${DayEntity.NAME}`")
        database.execSQL("DROP TABLE `${UserEntity.NAME}`")

        database.execSQL("CREATE TABLE IF NOT EXISTS `${UserEntity.NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `email` TEXT NOT NULL)")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_email` ON `${UserEntity.NAME}` (`email`)")

        database.execSQL("CREATE TABLE IF NOT EXISTS `${DayEntity.NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `count` INTEGER NOT NULL, `max` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_days_type_user_id` ON `${DayEntity.NAME}` (`type`, `user_id`)")

        database.execSQL("CREATE TABLE IF NOT EXISTS `${WeightResultEntity.NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `value` REAL NOT NULL, `date` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
    }
}