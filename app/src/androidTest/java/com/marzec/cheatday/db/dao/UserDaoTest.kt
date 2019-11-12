package com.marzec.cheatday.db.dao

import android.content.Context
import androidx.room.EmptyResultSetException
import androidx.room.Room.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.UserEntity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.getUserDao()
    }

    @Test
    fun getUser() {
        userDao.insertCompletable(UserEntity("uuid", "email")).test()
        userDao.getUser("email").test().assertValue(UserEntity("uuid", "email"))
    }

    @Test
    fun deleteUser() {
        userDao.insertCompletable(UserEntity("uuid", "email")).test()
        userDao.removeCompletable(UserEntity("uuid", "email")).test()
        userDao.getUser("email").test().assertError {
            it is EmptyResultSetException
        }
    }

    @Test
    fun updateUser() {
        userDao.insertCompletable(UserEntity("uuid", "email")).test()
        userDao.updateCompletable(UserEntity("uuid", "email2")).test()
        userDao.getUser("email2").test().assertValue(UserEntity("uuid", "email2"))
    }
}