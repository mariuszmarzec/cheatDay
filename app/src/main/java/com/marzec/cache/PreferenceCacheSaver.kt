package com.marzec.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class PreferenceCacheSaver<T : Any>(
    private val key: String,
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
    private val gson: Gson,
    private val typeToken: TypeToken<T>
) : CacheSaver<T> {

    override suspend fun get(): T? = withContext(dispatcher) {
        dataStore.data.firstOrNull()?.parseValue()
    }

    override suspend fun observeCached(): Flow<T?> = dataStore.data
        .mapLatest { it.parseValue() }
        .flowOn(dispatcher)

    override suspend fun updateCache(data: T): Unit = withContext(dispatcher) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[stringPreferencesKey(key)] = gson.toJson(data)
            }
        }
    }

    override suspend fun updateCache(update: (T?) -> T?): Unit = withContext(dispatcher) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                val newValue = update(this.parseValue())
                if (newValue != null) {
                    this[stringPreferencesKey(key)] = gson.toJson(newValue)
                } else {
                    this.remove(stringPreferencesKey(key))
                }
            }
        }
    }

    private fun Preferences?.parseValue(): T? =
        this?.get(stringPreferencesKey(key))?.let {
            gson.fromJson(it, typeToken.type)
        }
}

@Suppress("FunctionName")
inline fun <ID, reified MODEL : Any> PreferenceCacheListSaver(
    key: String,
    dataStore: DataStore<Preferences>,
    dispatcher: CoroutineDispatcher,
    gson: Gson,
    noinline isSameId: MODEL.(id: ID) -> Boolean
) = ListCacheSaverImpl(
    cacheSaver = PreferenceCacheSaver(
        key = key,
        dataStore = dataStore,
        dispatcher = dispatcher,
        gson = gson,
        typeToken = object : TypeToken<List<MODEL>>() {}
    ),
    isSameId = isSameId,
    newItemInsert = atLastPositionInserter()
)
