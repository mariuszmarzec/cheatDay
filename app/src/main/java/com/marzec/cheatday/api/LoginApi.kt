package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.LoginRequest
import com.marzec.cheatday.api.response.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @GET("logout")
    suspend fun logout()
}
