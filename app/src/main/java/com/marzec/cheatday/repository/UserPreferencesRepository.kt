package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.marzec.cheatday.model.domain.Day
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import org.joda.time.DateTime

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun setMaxPossibleWeight(weight: Float): Unit = withContext(dispatcher) {
        val userId = userRepository.getCurrentUser().id
        val preferencesKey = floatPreferencesKey("${userId}_max_possible_weight")
        dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply { this[preferencesKey] = weight }
        }
    }

    fun observeMaxPossibleWeight(): Flow<Float> =
        userRepository.observeCurrentUser().flatMapMerge { user ->
            dataStore.data.mapLatest { prefs ->
                val key = floatPreferencesKey("${user.id}_max_possible_weight")
                prefs[key] ?: .0f
            }
        }.flowOn(dispatcher)

    suspend fun setTargetWeight(weight: Float): Unit = withContext(dispatcher) {
        val userId = userRepository.getCurrentUser().id
        val preferencesKey = floatPreferencesKey("${userId}_weight")
        dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply { this[preferencesKey] = weight }
        }
    }

    fun observeTargetWeight(): Flow<Float> =
        userRepository.observeCurrentUser().flatMapMerge { user ->
            dataStore.data.mapLatest { prefs ->
                val key = floatPreferencesKey("${user.id}_weight")
                prefs[key] ?: .0f
            }
        }.flowOn(dispatcher)

    fun observeWasClickToday(day: Day.Type): Flow<Boolean> =
        userRepository.observeCurrentUser()
            .flatMapMerge { user ->
                dataStore.data.mapLatest { prefs ->
                    prefs[longPreferencesKey("${user.id}_$day")] ?: 0
                }.mapLatest { savedTime ->
                    val today = DateTime.now().withTimeAtStartOfDay()
                    today == DateTime(savedTime)
                }.flowOn(dispatcher)
            }

    suspend fun setWasClickedToday(day: Day.Type): Unit = withContext(dispatcher) {
        val userId = userRepository.getCurrentUser().id
        val timeInMillis = DateTime.now().withTimeAtStartOfDay().millis
        dataStore.updateData { prefs ->
            val preferencesKey = longPreferencesKey("${userId}_$day")
            prefs.toMutablePreferences().apply { this[preferencesKey] = timeInMillis }
        }
    }
}
