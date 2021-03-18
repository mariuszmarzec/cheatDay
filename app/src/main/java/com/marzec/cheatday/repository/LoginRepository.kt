package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.LoginApi
import com.marzec.cheatday.api.asContent
import com.marzec.cheatday.api.request.LoginRequest
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val loginApi: LoginApi,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun login(email: String, password: String): Content<User> =
        withContext(dispatcher) {
            asContent {
                val response = loginApi.login(LoginRequest(email, password))
                val user = response.body()!!
                userRepository.setCurrentUserWithAuth(
                    CurrentUserDomain(
                        id = user.id,
                        auth = response.headers()[Api.Headers.AUTHORIZATION].orEmpty(),
                        email = user.email
                    )
                )
                val domainUser = user.toDomain()
                userRepository.addUserToDbIfNeeded(domainUser)
                domainUser
            }
        }

    suspend fun logout(): Content<Unit> =
        withContext(dispatcher) {
            userRepository.clearCurrentUser()
            asContent {
                loginApi.logout()
            }
        }
}