package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.LoginRequest
import com.marzec.cheatday.api.response.UserDto
import okhttp3.Headers
import retrofit2.Response

class LocalLoginApi : LoginApi {

    override suspend fun login(request: LoginRequest) =
        Response.success(
            UserDto(request.email.hashCode(), request.email),
            Headers.headersOf(Api.Headers.AUTHORIZATION, request.email)
        )

    override suspend fun logout() = Unit
}
