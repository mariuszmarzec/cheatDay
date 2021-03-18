package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.PutWeightRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface WeightApi {

    @POST("weight")
    suspend fun put(@Body request: PutWeightRequest)
}