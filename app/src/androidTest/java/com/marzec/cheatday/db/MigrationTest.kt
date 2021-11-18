package com.marzec.cheatday.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.db.migrations.MIGRATION_1_TO_2
import com.marzec.cheatday.db.migrations.MIGRATION_2_TO_3
import com.marzec.cheatday.db.migrations.MIGRATION_3_TO_4
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.UserEntity
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

    val dbName = "test-database"

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
        testHelper.createDatabase(dbName, 2).apply {

            execSQL("INSERT INTO 'users' VALUES ('1234', 'test@user.com')")
            execSQL("INSERT INTO 'users' VALUES ('4321', 'test2@user.com')")
            execSQL("INSERT INTO 'users' VALUES ('1111', 'test3@user.com')")

            execSQL("INSERT INTO 'days' ('type', 'count', 'max', 'user_id') VALUES ('TYPE', 1, 3, '1234')")
            execSQL("INSERT INTO 'days' ('type', 'count', 'max', 'user_id') VALUES ('TYPE', 2, 3, '4321')")
            close()
        }

        testHelper.runMigrationsAndValidate(dbName, 3, true, MIGRATION_2_TO_3).apply {

            val users = getUsers()
            val days = getDays()

            assertThat(users).isEqualTo(
                listOf(
                    UserEntity(1, "test@user.com"),
                    UserEntity(2, "test2@user.com")
                )
            )

            assertThat(days).isEqualTo(
                listOf(
                    DayEntity(1, "TYPE", 1, 3, 1),
                    DayEntity(2, "TYPE", 2, 3, 2)
                )
            )

        }
    }

    @Test
    fun testMigration_from_3_to_4() {
        testHelper.createDatabase(dbName, 3)
        testHelper.runMigrationsAndValidate(dbName, 4, true, MIGRATION_3_TO_4)
    }

    private fun SupportSQLiteDatabase.getUsers(): MutableList<UserEntity> {
        val users = mutableListOf<UserEntity>()
        val cursor = query("SELECT * FROM 'users'")
        while (cursor.moveToNext()) {
            users.add(
                UserEntity(
                    cursor.getLong(0),
                    cursor.getString(1)
                )
            )
        }
        return users
    }

    private fun SupportSQLiteDatabase.getDays(): MutableList<DayEntity> {
        val days = mutableListOf<DayEntity>()
        val cursor = query("SELECT * FROM 'days'")
        while (cursor.moveToNext()) {
            days.add(
                DayEntity(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getLong(2),
                    cursor.getLong(3),
                    cursor.getLong(4)
                )
            )
        }
        return days
    }
}
