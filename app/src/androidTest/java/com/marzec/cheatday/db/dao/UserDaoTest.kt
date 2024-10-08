package com.marzec.cheatday.db.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.db.UserEntity
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.getUserDao()
    }

    @Test
    fun getUser() = runTest(testDispatcher) {
        userDao.insert(UserEntity(0, "email"))

        val user = userDao.getUser("email")

        assertThat(user).isEqualTo(
            UserEntity(
                id = 1,
                email = "email"
            )
        )
    }

    @Test
    fun updateUser() = runTest(testDispatcher) {
        userDao.insert(UserEntity(0, "email"))
        userDao.update(UserEntity(1, "email2"))

        val actual = userDao.getUser("email2")

        assertThat(actual).isEqualTo(
            UserEntity(
                id = 1,
                email = "email2"
            )
        )
    }
}
