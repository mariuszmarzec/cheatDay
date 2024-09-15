package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.marzec.cheatday.core.test
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserRepositoryTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    val preferences = mockk<Preferences>(relaxed = true)
    val mutablePreferences = mockk<MutablePreferences>(relaxed = true)

    val gson = Gson()
    private val userDao: UserDao = mockk(relaxed = true)
    private val preferencesDataStore: DataStore<Preferences> = mockk()

    lateinit var repository: UserRepository

    private val currentUser = CurrentUserDomain(
        id = 1,
        auth = "auth_token",
        email = "email"
    )

    private val currentUserNull = CurrentUserDomain(
        id = -1,
        auth = "",
        email = ""
    )

    private val user = User(
        1, "email"
    )

    private val userEntity = UserEntity(
        1, "email"
    )

    private val currentUserDomain = CurrentUserDomain(
        1, "auth_token", "email"
    )

    private val CURRENT_USER = "CURRENT_USER"

    @BeforeEach
    fun setUp() {
        repository = UserRepository(gson, userDao, preferencesDataStore, dispatcher)

        every { preferencesDataStore.data } returns flowOf(preferences)
        every { preferences.toMutablePreferences() } returns mutablePreferences

        coEvery { userDao.observeUser("email") } returns flowOf(userEntity)
        coEvery { userDao.getUser("email") } returns userEntity
    }

    @Test
    fun getCurrentUser()  = scope.runTest {
        mockCurrentUserFromPrefs(currentUser)
        coEvery { userDao.getUser("email") } returns userEntity

        assertThat(repository.getCurrentUser()).isEqualTo(user)
    }

    @Test
    fun getCurrentUserWithAuth()  = scope.runTest {
        mockCurrentUserFromPrefs(currentUser)

        assertThat(repository.getCurrentUserWithAuthToken()).isEqualTo(currentUserDomain)
    }

    @Test
    fun `given current user auth is empty, when getting auth token, returns null`()  = scope.runTest {
        mockCurrentUserFromPrefs(currentUser.copy(auth = ""))

        assertThat(repository.getCurrentUserWithAuthToken()).isNull()
    }


    @Test
    fun `given user data is available, when getting current user, returns domain user`()  = scope.runTest {
        mockCurrentUserFromPrefs(currentUser)
        coEvery { userDao.observeUser("email") } returns flowOf(userEntity)

        repository.observeCurrentUser().test(this).isEqualTo(listOf(user))
    }

    @Test
    fun `given current user mail is not empty, when getting if user logged, returns true`()  = scope.runTest {
        mockCurrentUserFromPrefs(currentUser)

        repository.observeIfUserLogged().test(this).isEqualTo(listOf(true))
    }

    @Test
    fun `given current user token are empty, when getting if user logged, returns false`()  = scope.runTest {
        mockCurrentUserFromPrefs(currentUserNull)

        repository.observeIfUserLogged().test(this).isEqualTo(listOf(false))
    }

    @Test
    fun `given user mail is not stored in database, when adding user, then user is added`()  = scope.runTest {
        coEvery { userDao.observeUser("email") } returns flowOf()

        repository.addUserToDbIfNeeded(user)

        coVerify { userDao.insert(UserEntity(0, "email")) }
    }

    @Test
    fun `given user mail is stored in database, when adding user, then new user is not added`() =
        scope.runTest {
            coEvery { userDao.observeUser("email") } returns flowOf(userEntity)

            repository.addUserToDbIfNeeded(user)

            verify(inverse = true) { userDao.insert(any()) }
        }

    @Test
    fun clearCurrentUser()  = scope.runTest {
        val expectedJsonArg = gson.toJson(currentUserNull)
        val captured = slot<suspend (Preferences) -> Preferences>()
            coEvery {
            preferencesDataStore.updateData(capture(captured))
        } answers { mutablePreferences }

        repository.clearCurrentUser()
        captured.captured(preferences)

        verify {
            mutablePreferences[stringPreferencesKey(CURRENT_USER)] = expectedJsonArg
        }
    }

    @Test
    fun setCurrentUserWithAuth()  = scope.runTest {
        val expectedJsonArg = gson.toJson(currentUser)
        val captured = slot<suspend (Preferences) -> Preferences>()
        coEvery {
            preferencesDataStore.updateData(capture(captured))
        } answers { mutablePreferences }

        repository.setCurrentUserWithAuth(currentUser)
        captured.captured(preferences)

        verify {
            mutablePreferences[stringPreferencesKey(CURRENT_USER)] = expectedJsonArg
        }
    }

    private fun mockCurrentUserFromPrefs(currentUser: CurrentUserDomain) {
        val json = gson.toJson(currentUser)
        every { preferences[stringPreferencesKey(CURRENT_USER)] } returns json
    }
}
