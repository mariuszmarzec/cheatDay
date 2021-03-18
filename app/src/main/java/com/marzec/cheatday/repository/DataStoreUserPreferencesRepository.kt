package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.toMutablePreferences
import com.marzec.cheatday.model.domain.Day
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import org.joda.time.DateTime

@FlowPreview
@ExperimentalCoroutinesApi
class DataStoreUserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository
) : UserPreferencesRepository {

    override suspend fun setWeightsMigrated(): Unit = withContext(Dispatchers.IO) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val preferencesKey = preferencesKey<Boolean>("${userId}_weight_migrated")
        dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply { this[preferencesKey] = true }
        }
    }

    override suspend fun isWeightsMigrated(): Boolean = withContext(Dispatchers.IO) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val preferencesKey = preferencesKey<Boolean>("${userId}_weight_migrated")
        dataStore.data.first()[preferencesKey] ?: true // TODO CHANGE TO FALSE BEFORE RELEASE
    }

    override suspend fun setTargetWeight(weight: Float): Unit = withContext(Dispatchers.IO) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val preferencesKey = preferencesKey<Float>("${userId}_weight")
        dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply { this[preferencesKey] = weight }
        }
    }

    override fun observeTargetWeight(): Flow<Float> =
        userRepository.getCurrentUserFlow().flatMapMerge { user ->
            dataStore.data.mapLatest { prefs ->
                val key = preferencesKey<Float>("${user.uuid}_weight")
                prefs[key] ?: .0f
            }
        }.flowOn(Dispatchers.IO)


    override fun observeWasClickToday(day: Day.Type): Flow<Boolean> =
        userRepository.getCurrentUserFlow()
            .flatMapMerge { user ->
                dataStore.data.mapLatest { prefs ->
                    prefs[preferencesKey<Long>("${user.uuid}_$day")] ?: 0
                }.mapLatest { savedTime ->
                    val today = DateTime.now().withTimeAtStartOfDay()
                    today == DateTime(savedTime)
                }.flowOn(Dispatchers.IO)
            }

    override suspend fun setWasClickedToday(day: Day.Type): Unit = withContext(Dispatchers.IO) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val timeInMillis = DateTime.now().withTimeAtStartOfDay().millis
        dataStore.updateData { prefs ->
            val preferencesKey = preferencesKey<Long>("${userId}_$day")
            prefs.toMutablePreferences().apply { this[preferencesKey] = timeInMillis }
        }
    }
}