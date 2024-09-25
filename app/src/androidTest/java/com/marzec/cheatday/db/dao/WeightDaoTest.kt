package com.marzec.cheatday.db.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.db.model.db.WeightResultEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeightDaoTest {

    val userId = 1L
    val otherUserId = 2L
    val userEntity = UserEntity(userId, "user@email.com")
    val otherUser = UserEntity(otherUserId, "user2@email.com")

    val weight = WeightResultEntity(
        1,
        1f,
        1,
        userEntity.id
    )
    val weight2 = WeightResultEntity(
        2,
        2f,
        0,
        userEntity.id
    )
    val weight3 = WeightResultEntity(
        3,
        3f,
        2,
        userEntity.id
    )
    val weight4 = WeightResultEntity(
        4,
        1f,
        0,
        otherUser.id
    )
    val weight5 = WeightResultEntity(
        5,
        3f,
        2,
        otherUser.id
    )

    val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var userDao: UserDao
    lateinit var weightDao: WeightDao
    lateinit var db: AppDatabase

    @Before
    fun setUp() {
        runTest(testDispatcher) {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            userDao = db.getUserDao()
            weightDao = db.getWeightDao()
            userDao.insert(userEntity)
            userDao.insert(otherUser)

            weightDao.insert(weight)
            weightDao.insert(weight2)
            weightDao.insert(weight3)
            weightDao.insert(weight4)
            weightDao.insert(weight5)
        }
    }

    @Test
    fun getWeight() = runTest(testDispatcher) {
        val actual = weightDao.getWeight(2)

        assertThat(actual).isEqualTo(weight2)
    }

    @Test
    fun observeWeights() = runTest(testDispatcher) {
        val actual = weightDao.observeWeights(userEntity.id).first()
            .toList()
        assertThat(actual).isEqualTo(listOf(weight3, weight, weight2))
    }

    @Test
    fun observeMinWeight() = runTest(testDispatcher) {
        val actual = weightDao.observeMinWeight(userEntity.id).first()

        assertThat(actual).isEqualTo(weight)
    }

    @Test
    fun observeLastWeight() = runTest(testDispatcher) {
        val actual = weightDao.observeLastWeight(userEntity.id).first()

        assertThat(actual).isEqualTo(weight3)
    }
}
