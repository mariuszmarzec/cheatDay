package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserPreferencesRepositoryTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    private val dataStore: DataStore<Preferences> = mockk()
    private val userRepository: UserRepository = mockk()

    private val preferences = mockk<Preferences>()
    private val mutablePreferences = mockk<MutablePreferences>(relaxed = true)

    private val repository = UserPreferencesRepository(
        dataStore, userRepository, dispatcher
    )

    private val user = User(
        1, "email"
    )

    @BeforeEach
    fun setUp() {
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
    fun setMaxPossibleWeight()  = scope.runTest {
        repository.setMaxPossibleWeight(10f)

        verify { mutablePreferences[floatPreferencesKey("1_max_possible_weight")] = eq(10f) }
    }

    @Test
    fun observeMaxPossibleWeight()  = scope.runTest {
        every { preferences[floatPreferencesKey("1_max_possible_weight")] } returns 5f

        val result = repository.observeMaxPossibleWeight().test(this)

        assertThat(result.values()).isEqualTo(listOf(5f))
    }

    @Test
    fun setTargetWeight()  = scope.runTest {
        repository.setTargetWeight(10f)

        verify { mutablePreferences[floatPreferencesKey("1_weight")] = eq(10f) }
    }

    @Test
    fun observeTargetWeight()  = scope.runTest {
        every { preferences[floatPreferencesKey("1_weight")] } returns 5f

        val result = repository.observeTargetWeight().test(this)

        assertThat(result.values()).isEqualTo(listOf(5f))
    }

    @Test
    fun observeWasClickToday()  = scope.runTest {
        DateTimeUtils.setCurrentMillisFixed(0)
        every { preferences[longPreferencesKey("1_CHEAT")] } returns DateTime.now()
            .withTimeAtStartOfDay().millis

        val result = repository.observeWasClickToday(Day.Type.CHEAT).test(this)

        assertThat(result.values()).isEqualTo(listOf(true))
    }

    @Test
    fun `Given last day change was in different time as today, when getting clicked today status, then returns false`()  = scope.runTest {
        DateTimeUtils.setCurrentMillisFixed(0)
        every { preferences[longPreferencesKey("1_CHEAT")] } returns 123

        val result = repository.observeWasClickToday(Day.Type.CHEAT).test(this)

        assertThat(result.values()).isEqualTo(listOf(false))
    }

    @Test
    fun setWasClickedToday()  = scope.runTest {
        DateTimeUtils.setCurrentMillisFixed(0)

        repository.setWasClickedToday(Day.Type.CHEAT)

        verify {
            mutablePreferences[longPreferencesKey("1_CHEAT")] =
                DateTime.now().withTimeAtStartOfDay().millis
        }
    }
}
