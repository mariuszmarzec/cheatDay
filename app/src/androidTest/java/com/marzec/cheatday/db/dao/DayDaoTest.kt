package com.marzec.cheatday.db.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.UserEntity
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DayDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var userDao: UserDao
    lateinit var dayDao: DayDao
    lateinit var db: AppDatabase
    val userEntity = UserEntity(1, "email")
    val testEntity = DayEntity(
        1,
        "type",
        10,
        3,
        1
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.getUserDao()
        dayDao = db.getDayDao()
        userDao.insert(userEntity)
    }

    @Test
    fun insert() {
        dayDao.insert(
            DayEntity(
                1,
                "type",
                10,
                3,
                1
            )
        )
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertDuplicate() {
        val entity = DayEntity(
            id = 1,
            type = "type",
            count = 10,
            max = 3,
            userId = 1
        )
        dayDao.insert(entity)
        dayDao.insert(entity)
    }

    @Test
    fun getDay() = test {
        dayDao.insert(
            DayEntity(
                1,
                "type",
                10,
                3,
                1
            )
        )
        assertEquals(
            DayEntity(
                1,
                "type",
                10,
                3,
                1
            ), dayDao.getDay(userEntity.id, "type")
        )
        val actual = dayDao.observeDay(userEntity.id, "type").first()

        assertThat(actual).isEqualTo(
            DayEntity(
                id = 1,
                type = "type",
                count = 10,
                max = 3,
                userId = 1
            )
        )
    }

    @Test
    fun createOrUpdate() = test {
        dayDao.insert(testEntity)
        dayDao.createOrUpdate(testEntity.copy(count = 20))

        val actual = dayDao.observeDay(1, "type").first()

        assertThat(actual).isEqualTo(testEntity.copy(count = 20))
    }

    @Test
    fun cascadeDeleteWithUser() = test {
        dayDao.insert(testEntity)
        val user = userDao.getUser("email")
        userDao.remove(user)

        val actual = dayDao.observeDay(testEntity.userId, testEntity.type).firstOrNull()

        assertThat(actual).isNull()
    }
}
