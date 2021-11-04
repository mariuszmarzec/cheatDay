package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.core.test
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.extensions.EMPTY_STRING
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.CurrentUserProto
import com.marzec.cheatday.model.domain.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserRepositoryTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val currentUserStore: DataStore<CurrentUserProto> = mockk()
    private val dispatcher = TestCoroutineDispatcher()

    lateinit var repository: UserRepository

    private val currentUserProto = CurrentUserProto.getDefaultInstance().newBuilderForType()
        .setAuthToken("auth_token")
        .setEmail("email")
        .setId(1)
        .build()

    private val emptyCurrentUserProto = CurrentUserProto.getDefaultInstance().newBuilderForType()
        .setAuthToken(EMPTY_STRING)
        .setEmail(EMPTY_STRING)
        .setId(-1)
        .build()

    private val user = User(
        1, "email"
    )

    private val userEntity = UserEntity(
        1, "email"
    )

    private val currentUserDomain = CurrentUserDomain(
        1, "auth_token", "email"
    )

    @BeforeEach
    fun setUp() {
        repository = UserRepository(userDao, currentUserStore, dispatcher)

        every { currentUserStore.data } returns flowOf(currentUserProto)
    }

    @Test
    fun getCurrentUser() = runBlockingTest {
        coEvery { userDao.getUser("email") } returns userEntity

        assertThat(repository.getCurrentUser()).isEqualTo(user)
    }

    @Test
    fun getCurrentUserWithAuth() = runBlockingTest {
        assertThat(repository.getCurrentUserWithAuthToken()).isEqualTo(currentUserDomain)
    }

    @Test
    fun getCurrentUserWithAuth_returnsNull_ifEmailEmpty() = runBlockingTest {
        every { currentUserStore.data } returns flowOf(
            currentUserProto.toBuilder().setEmail("").build()
        )
        assertThat(repository.getCurrentUserWithAuthToken()).isNull()
    }

    @Test
    fun observeCurrentUser() = runBlockingTest {
        coEvery { userDao.observeUser("email") } returns flowOf(userEntity)

        assertThat(repository.observeCurrentUser().test(this).values()).isEqualTo(listOf(user))
    }

    @Test
    fun observeIfUserLogged() = runBlockingTest {
        assertThat(repository.observeIfUserLogged().test(this).values()).isEqualTo(listOf(true))
    }

    @Test
    fun observeIfUserLogged_returnsFalse_ifNoAuthTokenAndEmail() = runBlockingTest {
        every { currentUserStore.data } returns flowOf(
            currentUserProto,
            currentUserProto.toBuilder().setEmail("").setAuthToken("").build()
        )

        assertThat(repository.observeIfUserLogged().test(this).values())
            .isEqualTo(
                listOf(true, false)
            )
    }

    @Test
    fun addUserToDbIfNeeded() = runBlockingTest {
        coEvery { userDao.observeUser("email") } returns flowOf()

        repository.addUserToDbIfNeeded(user)

        coVerify { userDao.insert(UserEntity(0, "email")) }
    }

    @Test
    fun addUserToDbIfNeeded_doesntCallUserDao_ifUserExists() = runBlockingTest {
        coEvery { userDao.observeUser("email") } returns flowOf(userEntity)

        repository.addUserToDbIfNeeded(user)

        verify(inverse = true) { userDao.insert(any()) }
    }

    @Test
    fun clearCurrentUser() = runBlockingTest {
        val captor = slot<suspend (CurrentUserProto) -> CurrentUserProto>()
        coEvery { currentUserStore.updateData(capture(captor)) } answers {
            CurrentUserProto.getDefaultInstance()
        }

        repository.clearCurrentUser()

        assertThat(captor.captured(currentUserProto)).isEqualTo(emptyCurrentUserProto)
    }

    @Test
    fun setCurrentUserWithAuth() = runBlockingTest {
        val captor = slot<suspend (CurrentUserProto) -> CurrentUserProto>()
        coEvery { currentUserStore.updateData(capture(captor)) } answers {
            CurrentUserProto.getDefaultInstance()
        }

        repository.setCurrentUserWithAuth(currentUserDomain)

        assertThat(captor.captured(emptyCurrentUserProto)).isEqualTo(currentUserProto)
    }
}
