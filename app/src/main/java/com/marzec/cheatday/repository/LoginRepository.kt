package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.content.Content
import com.marzec.cheatday.api.LoginApi
import com.marzec.content.asContent
import com.marzec.content.asContentFlow
import com.marzec.cheatday.api.request.LoginRequest
import com.marzec.cheatday.api.request.toDomain
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class LoginRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val loginApi: LoginApi,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun login(email: String, password: String): Flow<Content<User>> = asContentFlow {
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
    }.flowOn(dispatcher)

    suspend fun logout(): Content<Unit> =
        withContext(dispatcher) {
            userRepository.clearCurrentUser()
            asContent {
                loginApi.logout()
            }
        }
}
