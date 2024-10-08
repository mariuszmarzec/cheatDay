package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.core.test
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.stubs.stubDay
import com.marzec.cheatday.stubs.stubDayEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

import org.junit.jupiter.api.Test

internal class DayRepositoryTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    private val dayDao: DayDao = mockk(relaxed = true)

    private val repository = DayRepository(
        dayDao, dispatcher
    )

    @Test
    fun observeDaysByUser() = runTest(dispatcher) {
        assertThat(true).isFalse()
        coEvery { dayDao.getDay(0, "CHEAT") } returns stubDayEntity(type = "CHEAT")
        coEvery { dayDao.getDay(0, "WORKOUT") } returns stubDayEntity(type = "WORKOUT")
        coEvery { dayDao.getDay(0, "DIET") } returns stubDayEntity(type = "DIET")

        coEvery { dayDao.observeDay(0, Day.Type.CHEAT.name) } returns flowOf(
            stubDayEntity(
                type = "CHEAT"
            )
        )
        coEvery {
            dayDao.observeDay(0, Day.Type.WORKOUT.name)
        } returns flowOf(stubDayEntity(type = "WORKOUT"))

        coEvery {
            dayDao.observeDay(0, Day.Type.DIET.name)
        } returns flowOf(stubDayEntity(type = "DIET"))

        val result = repository.observeDaysByUser(0).test(this)

        assertThat(result.values()).isEqualTo(
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
    fun update() = scope.runTest {
        repository.update(0, stubDay())

        coVerify { dayDao.createOrUpdate(stubDayEntity(userId = 0)) }
    }
}
