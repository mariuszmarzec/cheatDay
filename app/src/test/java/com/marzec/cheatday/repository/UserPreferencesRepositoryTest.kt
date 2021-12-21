package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.toMutablePreferences
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.core.test
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.User
import io.mockk.coEvery
import io.mockk.coInvoke
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach

internal class UserPreferencesRepositoryTest {

    private val dataStore: DataStore<Preferences> = mockk()
    private val userRepository: UserRepository = mockk()
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()

    private val preferences = mockk<Preferences>()
    private val mutablePreferences = mockk<MutablePreferences>(relaxed = true)

    private val repository = UserPreferencesRepository(
        dataStore, userRepository, dispatcher
    )

    private val user = User(
        1, "email"
    )

    @BeforeEach
    fun setUp() = runBlockingTest {
        mockkStatic("androidx.datastore.preferences.core.PreferencesKt")
        coEvery { userRepository.getCurrentUser() } returns user
        coEvery { userRepository.observeCurrentUser() } returns flowOf(user)
        every { preferences.toMutablePreferences() } returns mutablePreferences
        every { dataStore.data } returns flowOf(preferences)
        coEvery {
            dataStore.updateData(captureLambda())
        } coAnswers {
            lambda<suspend (Preferences) -> Preferences>().coInvoke(preferences)
            preferences
        }
    }

    @Test
    fun setMaxPossibleWeight() = runBlockingTest {
        repository.setMaxPossibleWeight(10f)

        verify { mutablePreferences[preferencesKey<Float>("1_max_possible_weight")] = eq(10f) }
    }

    @Test
    fun observeMaxPossibleWeight() = runBlockingTest {
        every { preferences[preferencesKey<Float>("1_max_possible_weight")] } returns 5f

        val result = repository.observeMaxPossibleWeight().test(this)

        assertThat(result.values()).isEqualTo(listOf(5f))
    }

    @Test
    fun setTargetWeight() = runBlockingTest {
        repository.setTargetWeight(10f)

        verify { mutablePreferences[preferencesKey<Float>("1_weight")] = eq(10f) }
    }

    @Test
    fun observeTargetWeight() = runBlockingTest {
        every { preferences[preferencesKey<Float>("1_weight")] } returns 5f

        val result = repository.observeTargetWeight().test(this)

        assertThat(result.values()).isEqualTo(listOf(5f))
    }

    @Test
    fun observeWasClickToday() = runBlockingTest {
        DateTimeUtils.setCurrentMillisFixed(0)
        every { preferences[preferencesKey<Long>("1_CHEAT")] } returns DateTime.now()
            .withTimeAtStartOfDay().millis

        val result = repository.observeWasClickToday(Day.Type.CHEAT).test(this)

        assertThat(result.values()).isEqualTo(listOf(true))
    }

    @Test
    fun `Given last day change was in different time as today, when getting clicked today status, then returns false`() = runBlockingTest {
        DateTimeUtils.setCurrentMillisFixed(0)
        every { preferences[preferencesKey<Long>("1_CHEAT")] } returns 123

        val result = repository.observeWasClickToday(Day.Type.CHEAT).test(this)

        assertThat(result.values()).isEqualTo(listOf(false))
    }

    @Test
    fun setWasClickedToday() = runBlockingTest {
        DateTimeUtils.setCurrentMillisFixed(0)

        repository.setWasClickedToday(Day.Type.CHEAT)

        verify {
            mutablePreferences[preferencesKey<Long>("1_CHEAT")] =
                DateTime.now().withTimeAtStartOfDay().millis
        }
    }
}
