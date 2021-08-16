package com.marzec.cheatday.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.marzec.cheatday.db.migrations.MIGRATION_1_TO_2
import com.marzec.cheatday.db.migrations.MIGRATION_2_TO_3
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @Rule
    @JvmField
    val testHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    val dbName = "database"

    @Test
    fun testMigration() {
        testHelper.createDatabase(dbName, 1)
        testHelper.runMigrationsAndValidate(dbName, 1, true)
    }

    @Test
    fun testMigration_from_1_to_2() {
        testHelper.createDatabase(dbName, 1)
        testHelper.runMigrationsAndValidate(dbName, 2, true, MIGRATION_1_TO_2)
    }

    @Test
    fun testMigration_from_2_to_3() {
        testHelper.createDatabase(dbName, 2)
        testHelper.runMigrationsAndValidate(dbName, 3, true, MIGRATION_2_TO_3)
    }
}