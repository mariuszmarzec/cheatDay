package com.marzec.cheatday.db.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.db.model.db.WeightResultEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeightDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDao: UserDao
    private lateinit var weightDao: WeightDao
    private lateinit var db: AppDatabase

    private val userEntity = UserEntity("uuid", "email")
    private val otherUser = UserEntity("other_user_uuid", "email")

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.getUserDao()
        weightDao = db.getWeightDao()
        userDao.insertCompletable(userEntity).test()
        userDao.insertCompletable(otherUser).test()

    }

    @Test
    fun getAllWeights() {
        weightDao.insert(
            WeightResultEntity(
                0,
                1f,
                1,
                userEntity.uuid
            )
        )
        weightDao.insert(
            WeightResultEntity(
                0,
                1f,
                0,
                userEntity.uuid
            )
        )
        weightDao.insert(
            WeightResultEntity(
                0,
                1f,
                0,
                otherUser.uuid
            )
        )
        weightDao.getWeights(userEntity.uuid)
            .test()
            .assertValue(listOf(
                WeightResultEntity(
                    1,
                    1f,
                    1,
                    userEntity.uuid
                ),
                WeightResultEntity(
                    2,
                    1f,
                    0,
                    userEntity.uuid
                )
            ))
    }
}