package com.marzec.cheatday.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.marzec.cheatday.db.model.db.WeightResultEntity

val MIGRATION_3_TO_4 = Migration3to4()

@Suppress("MaxLineLength", "MagicNumber")
class Migration3to4 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE ${WeightResultEntity.NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS `${WeightResultEntity.NAME}` (`id` INTEGER NOT NULL, `value` REAL NOT NULL, `date` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
    }
}
