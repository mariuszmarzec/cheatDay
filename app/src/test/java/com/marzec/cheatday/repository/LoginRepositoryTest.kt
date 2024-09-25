package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Api
import com.marzec.content.Content
import com.marzec.cheatday.api.LoginApi
import com.marzec.cheatday.api.request.LoginRequest
import com.marzec.cheatday.api.response.UserDto
import com.marzec.cheatday.core.test
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import org.junit.jupiter.api.Test
import retrofit2.Response

class LoginRepositoryTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val loginApi: LoginApi = mockk()

    private val repository = LoginRepository(
        userRepository, loginApi, dispatcher
    )

    private val request = LoginRequest("email", "password")

    private val user = User(
        1, "email"
    )

    private val userDto = UserDto(
        1, "email"
    )

    @Test
    fun login() = scope.runTest {
        coEvery { loginApi.login(request) } returns Response.success(
            userDto,
            Headers.headersOf(Api.Headers.AUTHORIZATION, "auth_token")
        )

        val result = repository.login("email", "password")
            .test(this)

        coVerify {
            userRepository.setCurrentUserWithAuth(
                CurrentUserDomain(
                    1, "auth_token", "email"
                )
            )
        }
        coVerify {
            userRepository.addUserToDbIfNeeded(user)
        }
        result.isEqualTo(
            Content.Loading(),
            Content.Data(user)
        )
    }

    @Test
    fun logout() = scope.runTest {
        coEvery { loginApi.logout() } returns Unit

        val result = repository.logout()

        coVerify { userRepository.clearCurrentUser() }
        assertThat(result).isEqualTo(Content.Data(Unit))
    }
}
