package com.marzec.cheatday.db.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.db.model.db.WeightResultEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
class WeightDaoTest {
//
//    private val testDispatcher = TestCoroutineDispatcher()
//
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var userDao: UserDao
//    private lateinit var weightDao: WeightDao
//    private lateinit var db: AppDatabase
//
//    private val userId = 1L
//    private val otherUserId = 2L
//    private val userEntity = UserEntity(userId, "email")
//    private val otherUser = UserEntity(otherUserId, "email")
//
//    @Before
//    fun setUp() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = inMemoryDatabaseBuilder(context, AppDatabase::class.java)
//            .setTransactionExecutor(testDispatcher.asExecutor())
//            .setQueryExecutor(testDispatcher.asExecutor())
//            .build()
//        userDao = db.getUserDao()
//        weightDao = db.getWeightDao()
//        userDao.insert(userEntity)
//        userDao.insert(otherUser)
//    }
//
//    @Test
//    fun getAllWeights(): Unit = runBlockingTest {
////        weightDao.insert(
////            WeightResultEntity(
////                0,
////                1f,
////                1,
////                userEntity.id
////            )
////        )
////        weightDao.insert(
////            WeightResultEntity(
////                0,
////                1f,
////                0,
////                userEntity.id
////            )
////        )
////        weightDao.insert(
////            WeightResultEntity(
////                0,
////                1f,
////                0,
////                otherUser.id
////            )
////        )
////        val actualItems = weightDao.observeWeights(userEntity.id).first()
////            .toList()
////        Assert.assertEquals(
////            listOf(
////                WeightResultEntity(
////                    1,
////                    1f,
////                    1,
////                    userEntity.id
////                ),
////                WeightResultEntity(
////                    2,
////                    1f,
////                    0,
////                    userEntity.id
////                )
////            ),
////            actualItems
////        )
//    }
}