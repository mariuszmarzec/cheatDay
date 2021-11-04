package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.WeightDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface WeightApi {

    @POST("weight")
    suspend fun put(@Body request: PutWeightRequest)

    @PATCH("weight/{id}")
    suspend fun update(@Path("id") id: Long, @Body request: WeightDto)

    @DELETE("weight/{id}")
    suspend fun remove(@Path("id") id: Long)

    @GET("weights")
    suspend fun getAll(): List<WeightDto>
}
