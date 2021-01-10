package com.marzec.cheatday.repository

import com.marzec.cheatday.model.domain.Day
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    suspend fun setTargetWeight(weight: Float)

    fun observeTargetWeight(): Flow<Float>

    fun observeWasClickToday(day: Day.Type): Flow<Boolean>

    suspend fun setWasClickedToday(day: Day.Type)
}