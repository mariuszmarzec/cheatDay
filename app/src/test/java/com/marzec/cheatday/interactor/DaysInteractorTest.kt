package com.marzec.cheatday.interactor

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.core.test
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.DayRepository
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.stubs.stubDay
import com.marzec.cheatday.stubs.stubDaysGroup
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DaysInteractorTest {

    private val userRepository: UserRepository = mockk()
    private val daysRepository: DayRepository = mockk(relaxed = true)
    private val userPreferencesRepository: UserPreferencesRepository = mockk()

    lateinit var interactor: DaysInteractor

    @BeforeEach
    fun setUp() = runBlockingTest {
        coEvery { userRepository.getCurrentUser() } returns User(0, "user@email.com")
        coEvery { userRepository.observeCurrentUser() } returns flowOf(
            User(
                0,
                "user@email.com"
            )
        )
        coEvery { daysRepository.observeDaysByUser(0) } returns flowOf()

        interactor = DaysInteractor(
            userRepository,
            daysRepository,
            userPreferencesRepository
        )
    }

    @Test
    fun getDays() = runBlockingTest {
        coEvery { daysRepository.observeDaysByUser(0) } returns flowOf(
            stubDaysGroup(),
            stubDaysGroup(stubDay(count = 10))
        )

        assertThat(interactor.observeDays().test(this).values()).isEqualTo(
            listOf(
                stubDaysGroup(),
                stubDaysGroup(stubDay(count = 10))
            )
        )
    }

    @Test
    fun updateDay_cheatDay() = runBlockingTest {
        interactor.updateDay(stubDay(type = Day.Type.CHEAT))

        coVerify { userRepository.getCurrentUser() }
        coVerify { daysRepository.update(0, stubDay(type = Day.Type.CHEAT)) }
    }

    @Test
    fun updateDay_workoutDay_withoutCheatUpdate() = runBlockingTest {
        interactor.updateDay(stubDay(type = Day.Type.WORKOUT))

        coVerify { userRepository.getCurrentUser() }
        coVerify { daysRepository.update(0, stubDay(type = Day.Type.WORKOUT)) }
    }

    @Test
    fun updateDay_workoutDay_withCheatUpdate() = runBlockingTest {
        coEvery { daysRepository.observeDaysByUser(0) } returns flowOf(stubDaysGroup())

        interactor.updateDay(stubDay(type = Day.Type.WORKOUT, count = 3L))

        coVerify { daysRepository.update(0, stubDay(type = Day.Type.WORKOUT, count = 3L)) }
        coVerify { daysRepository.update(0, stubDay(type = Day.Type.CHEAT, count = 1)) }
    }

    @Test
    fun updateDay_dietDay_withCheatUpdate() = runBlockingTest {
        coEvery { daysRepository.observeDaysByUser(0) } returns flowOf(stubDaysGroup())

        interactor.updateDay(stubDay(type = Day.Type.DIET, count = 5L))

        coVerify {
            daysRepository.update(0, stubDay(type = Day.Type.CHEAT, count = 1))
            daysRepository.update(0, stubDay(type = Day.Type.DIET, count = 5L))
        }
    }

    @Test
    fun updateDay_workoutDay_withoutDietUpdate() = runBlockingTest {
        interactor.updateDay(stubDay(type = Day.Type.DIET))

        coVerify { userRepository.getCurrentUser() }
        coVerify { daysRepository.update(0, stubDay(type = Day.Type.DIET)) }
    }

    @Test
    fun getMaxDietDays() = runBlockingTest {
        assertThat(interactor.getMaxDietDays()).isEqualTo(Constants.MAX_DIET_DAYS)
    }

    @Test
    fun getMaxWorkoutDays() = runBlockingTest {
        assertThat(interactor.getMaxWorkoutDays()).isEqualTo(Constants.MAX_WORKOUT_DAYS)
    }

    @Test
    fun incrementCheatDays_plusDaysCount() = runBlockingTest {
        coEvery { daysRepository.observeDaysByUser(0) } returns flowOf(stubDaysGroup())

        interactor.incrementCheatDays(7)

        coVerify { daysRepository.update(0, stubDay(count = 7)) }
    }

    @Test
    fun incrementCheatDays_minusDaysCount() = runBlockingTest {
        coEvery { daysRepository.observeDaysByUser(0) } returns flowOf(stubDaysGroup())

        interactor.incrementCheatDays(-7)

        coVerify { daysRepository.update(0, stubDay(count = -7)) }
    }
}
