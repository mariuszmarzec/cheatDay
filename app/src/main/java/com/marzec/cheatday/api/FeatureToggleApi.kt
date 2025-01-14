package com.marzec.cheatday.api

import com.marzec.cheatday.api.response.FeatureToggleDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FeatureToggleApi {

    @GET("feature_toggles/{id}")
    suspend fun getById(@Path("id") id: Int): FeatureToggleDto

    @GET("feature_toggles")
    suspend fun getAll(): List<FeatureToggleDto>
}
