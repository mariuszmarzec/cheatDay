package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.LoginApi
import com.marzec.cheatday.api.request.LoginRequest
import com.marzec.cheatday.api.response.UserDto
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.User
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Headers
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import retrofit2.Response

class LoginRepositoryTest {

    private val userRepository: UserRepository = mock()
    private val loginApi: LoginApi = mock()
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()

    private val repository = LoginRepository(
        userRepository, loginApi, dispatcher
    )

    private val request = LoginRequest("email", "password")

    private val user = User(
        "1", "email"
    )

    private val userDto = UserDto(
        1, "email"
    )

    @Test
    fun login() = runBlockingTest {
        whenever(loginApi.login(request)).thenReturn(
            Response.success(
                userDto,
                Headers.headersOf(Api.Headers.AUTHORIZATION, "auth_token")
            )
        )

        val result = repository.login("email", "password")

        verify(userRepository).setCurrentUserWithAuth(
            CurrentUserDomain(
                1, "auth_token", "email"
            )
        )
        verify(userRepository).addUserToDbIfNeeded(user)
        assertThat(result).isEqualTo(Content.Data(user))
    }

    @Test
    fun logout() = runBlockingTest {
        whenever(loginApi.logout()).thenReturn(Unit)

        val result = repository.logout()

        verify(userRepository).clearCurrentUser()
        assertThat(result).isEqualTo(Content.Data(Unit))
    }
}