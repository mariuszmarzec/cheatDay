package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.core.values
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.model.domain.toDb
import com.marzec.cheatday.stubs.stubDay
import com.marzec.cheatday.stubs.stubDayEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DayRepositoryTest {

    private val dayDao: DayDao = mockk(relaxed = true)
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()

    private val repository = DayRepository(
        dayDao, dispatcher
    )

    @Test
    fun observeDaysByUser() = runBlockingTest {
        coEvery { dayDao.getDay("user_id", "CHEAT") } returns stubDayEntity(type = "CHEAT")
        coEvery { dayDao.getDay("user_id", "WORKOUT") } returns stubDayEntity(type = "WORKOUT")
        coEvery { dayDao.getDay("user_id", "DIET") } returns stubDayEntity(type = "DIET")

        coEvery { dayDao.observeDay("user_id", Day.Type.CHEAT.name) } returns flowOf(
            stubDayEntity(
                type = "CHEAT"
            )
        )
        coEvery {
            dayDao.observeDay(
                "user_id",
                Day.Type.WORKOUT.name
            )
        } returns flowOf(stubDayEntity(type = "WORKOUT"))
        coEvery { dayDao.observeDay("user_id", Day.Type.DIET.name) } returns flowOf(
            stubDayEntity(
                type = "DIET"
            )
        )

        val result = repository.observeDaysByUser("user_id").values(this)

        assertThat(result).isEqualTo(
            listOf(
                DaysGroup(
                    stubDay(type = Day.Type.CHEAT),
                    stubDay(type = Day.Type.WORKOUT),
                    stubDay(type = Day.Type.DIET)
                )
            )
        )
        coVerify(exactly = 0) { dayDao.createOrUpdate(any()) }
    }

    @Test
    fun observeDaysByUser_createIfNotExist() = runBlockingTest {
        coEvery { dayDao.getDay("user_id", "CHEAT") } returns null
        coEvery { dayDao.getDay("user_id", "WORKOUT") } returns null
        coEvery { dayDao.getDay("user_id", "DIET") } returns null

        coEvery { dayDao.observeDay("user_id", Day.Type.CHEAT.name) } returns flowOf(
            stubDayEntity(
                type = "CHEAT"
            )
        )
        coEvery {
            dayDao.observeDay(
                "user_id",
                Day.Type.WORKOUT.name
            )
        } returns flowOf(stubDayEntity(type = "WORKOUT"))
        coEvery { dayDao.observeDay("user_id", Day.Type.DIET.name) } returns flowOf(
            stubDayEntity(
                type = "DIET"
            )
        )

        val result = repository.observeDaysByUser("user_id").values(this)

        assertThat(result).isEqualTo(
            listOf(
                DaysGroup(
                    stubDay(type = Day.Type.CHEAT),
                    stubDay(type = Day.Type.WORKOUT),
                    stubDay(type = Day.Type.DIET)
                )
            )
        )
        coVerify {
            dayDao.createOrUpdate(
                stubDayEntity(
                    max = Constants.MAX_CHEAT_DAYS.toLong(),
                    type = "CHEAT",
                    userId = "user_id"
                )
            )
        }
        coVerify {
            dayDao.createOrUpdate(
                stubDayEntity(
                    max = Constants.MAX_WORKOUT_DAYS.toLong(),
                    type = "WORKOUT",
                    userId = "user_id"
                )
            )
        }
        coVerify {
            dayDao.createOrUpdate(
                stubDayEntity(
                    max = Constants.MAX_DIET_DAYS.toLong(),
                    type = "DIET",
                    userId = "user_id"
                )
            )
        }
    }

    @Test
    fun update() = runBlockingTest {
        repository.update("user_id", stubDay())

        coVerify { dayDao.createOrUpdate(stubDayEntity(userId = "user_id")) }
    }
}