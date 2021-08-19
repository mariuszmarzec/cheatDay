package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.core.test
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.extensions.emptyString
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.CurrentUserProto
import com.marzec.cheatday.model.domain.User
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserRepositoryTest {

    private val userDao: UserDao = mock()
    private val currentUserStore: DataStore<CurrentUserProto> = mock()
    private val dispatcher = TestCoroutineDispatcher()

    lateinit var repository: UserRepository

    private val currentUserProto = CurrentUserProto.getDefaultInstance().newBuilderForType()
        .setAuthToken("auth_token")
        .setEmail("email")
        .setId(1)
        .build()

    private val emptyCurrentUserProto = CurrentUserProto.getDefaultInstance().newBuilderForType()
        .setAuthToken(emptyString())
        .setEmail(emptyString())
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

        whenever(currentUserStore.data).thenReturn(flowOf(currentUserProto))
    }

    @Test
    fun getCurrentUser() = runBlockingTest {
        whenever(userDao.getUser("email")).thenReturn(userEntity)

        assertThat(repository.getCurrentUser()).isEqualTo(user)
    }

    @Test
    fun getCurrentUserWithAuth() = runBlockingTest {
        assertThat(repository.getCurrentUserWithAuthToken()).isEqualTo(currentUserDomain)
    }

    @Test
    fun getCurrentUserWithAuth_returnsNull_ifEmailEmpty() = runBlockingTest {
        whenever(currentUserStore.data).thenReturn(
            flowOf(
                currentUserProto.toBuilder().setEmail("").build()
            )
        )
        assertThat(repository.getCurrentUserWithAuthToken()).isNull()
    }

    @Test
    fun observeCurrentUser() = runBlockingTest {
        whenever(userDao.observeUser("email")).thenReturn(flowOf(userEntity))

        assertThat(repository.observeCurrentUser().test(this).values()).isEqualTo(listOf(user))
    }

    @Test
    fun observeIfUserLogged() = runBlockingTest {
        assertThat(repository.observeIfUserLogged().test(this).values()).isEqualTo(listOf(true))
    }

    @Test
    fun observeIfUserLogged_returnsFalse_ifNoAuthTokenAndEmail() = runBlockingTest {
        whenever(currentUserStore.data).thenReturn(
            flowOf(
                currentUserProto,
                currentUserProto.toBuilder().setEmail("").setAuthToken("").build()
            )
        )

        assertThat(repository.observeIfUserLogged().test(this).values())
            .isEqualTo(
                listOf(true, false)
            )
    }

    @Test
    fun addUserToDbIfNeeded() = runBlockingTest {
        whenever(userDao.observeUser("email")).thenReturn(flowOf())

        repository.addUserToDbIfNeeded(user)

        verify(userDao).insert(UserEntity(0, "email"))
    }

    @Test
    fun addUserToDbIfNeeded_doesntCallUserDao_ifUserExists() = runBlockingTest {
        whenever(userDao.observeUser("email")).thenReturn(flowOf(userEntity))

        repository.addUserToDbIfNeeded(user)

        verify(userDao, never()).insert(any())
    }

    @Test
    fun clearCurrentUser() = runBlockingTest {
        val captor = argumentCaptor<suspend (CurrentUserProto) -> CurrentUserProto>()

        repository.clearCurrentUser()

        verify(currentUserStore).updateData(captor.capture())
        assertThat(captor.firstValue.invoke(currentUserProto)).isEqualTo(emptyCurrentUserProto)
    }

    @Test
    fun setCurrentUserWithAuth() = runBlockingTest {
        val captor = argumentCaptor<suspend (CurrentUserProto) -> CurrentUserProto>()

        repository.setCurrentUserWithAuth(currentUserDomain)


        verify(currentUserStore).updateData(captor.capture())
        assertThat(captor.firstValue.invoke(emptyCurrentUserProto)).isEqualTo(currentUserProto)
    }
}
