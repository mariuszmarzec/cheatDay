package com.marzec.cheatday.db.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.UserEntity
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DayDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDao: UserDao
    private lateinit var dayDao: DayDao
    private lateinit var db: AppDatabase
    private val userEntity = UserEntity("uuid", "email")
    private val testEntity = DayEntity(
        1,
        "type",
        10,
        "uuid"
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.getUserDao()
        dayDao = db.getDayDao()
        userDao.insertCompletable(userEntity).test()
    }

    @Test
    fun insert() {
        dayDao.insertCompletable(
            DayEntity(
                1,
                "type",
                10,
                "uuid"
            )
        ).test().assertComplete()
    }

    @Test
    fun insertDuplicate() {
        dayDao.insertCompletable(
            DayEntity(
                1,
                "type",
                10,
                "uuid"
            )
        ).test().assertComplete()
        dayDao.insertCompletable(
            DayEntity(
                1,
                "type",
                10,
                "uuid"
            )
        ).test().assertError { it is SQLiteConstraintException }
    }

    @Test
    fun getDay() {
        dayDao.insert(
            DayEntity(
                1,
                "type",
                10,
                "uuid"
            )
        )
        assertEquals(
            DayEntity(
                1,
                "type",
                10,
                "uuid"
            ), dayDao.getDay(userEntity.uuid, "type")
        )
        dayDao.getDayObservable(userEntity.uuid, "unknown_type").test().assertEmpty()
        dayDao.getDayObservable(userEntity.uuid, "type").test().assertValue(
            DayEntity(
                1,
                "type",
                10,
                "uuid"
            )
        )
    }

    @Test
    fun createOrUpdate() {
        dayDao.insert(testEntity)
        dayDao.createOrUpdateCompletable(testEntity.copy(count = 20)).test()
        dayDao.getDayObservable("uuid", "type").test().assertValue(testEntity.copy(count = 20))
    }

    @Test
    fun cascadeDeleteWithUser() {
        dayDao.insert(testEntity)
        val user = userDao.getUser("email").test().values().first()
        userDao.remove(user)

        dayDao.getDayObservable(testEntity.userId, testEntity.type).test().assertEmpty()
    }
}