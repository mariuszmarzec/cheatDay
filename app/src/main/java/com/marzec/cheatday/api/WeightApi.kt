package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.request.UpdateWeightDto
import com.marzec.cheatday.api.response.WeightDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface WeightApi {

    @POST("weights")
    suspend fun put(@Body request: PutWeightRequest): WeightDto

    @PATCH("weights/{id}")
    suspend fun update(@Path("id") id: Long, @Body request: UpdateWeightDto): WeightDto

    @DELETE("weights/{id}")
    suspend fun remove(@Path("id") id: Long)

    @GET("weights")
    suspend fun getAll(): List<WeightDto>
}
