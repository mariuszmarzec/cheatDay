package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.extensions.orFalse
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.model.domain.toDomain
import com.marzec.cheatday.model.domain.toUser
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class UserRepository @Inject constructor(
    private val gson: Gson,
    private val userDao: UserDao,
    private val preferencesDataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getCurrentUser(): User = withContext(dispatcher) {
        val currentUser: CurrentUserDomain = preferencesDataStore.data.mapLatest { preferences ->
            preferences.getCurrentUser() ?: currentUserNull
        }.first()
        userDao.getUser(currentUser.email).toDomain()
    }

    suspend fun getCurrentUserWithAuthToken(): CurrentUserDomain? = withContext(dispatcher) {
        preferencesDataStore.data.first().getCurrentUser().takeIf {
            it?.auth?.isNotEmpty().orFalse()
        }
    }

    fun observeCurrentUser(): Flow<User> {
        return preferencesDataStore.data.mapLatest {
            it.getCurrentUserValue().toUser()
        }.flowOn(dispatcher)
    }

    fun observeIfUserLogged(): Flow<Boolean> =
        preferencesDataStore.data.mapLatest {
            it.getCurrentUser()?.auth?.isNotEmpty().orFalse()
        }

    suspend fun clearCurrentUser(): Unit = withContext(dispatcher) {
        preferencesDataStore.updateData {
            it.toMutablePreferences().apply {
                saveCurrentUser(currentUserNull)
            }
        }
    }

    suspend fun addUserToDbIfNeeded(user: User) {
        addUserToDbIfNeeded(user.email)
    }

    private suspend fun addUserToDbIfNeeded(email: String): UserEntity {
        return userDao.observeUser(email).firstOrNull() ?: run {
            userDao.insert(UserEntity(0, email))
            userDao.getUser(email)
        }
    }

    suspend fun setCurrentUserWithAuth(newUser: CurrentUserDomain): Unit = withContext(dispatcher) {
        val userEntity = addUserToDbIfNeeded(newUser.email)
        preferencesDataStore.updateData {
            it.toMutablePreferences().apply {
                saveCurrentUser(newUser.copy(id = userEntity.id.toInt()))
            }
        }
        Unit
    }

    private fun Preferences.getCurrentUser(): CurrentUserDomain? {
        val json = this[stringPreferencesKey(CURRENT_USER)].orEmpty()
        return gson.fromJson(json, CurrentUserDomain::class.java)
    }

    private fun MutablePreferences.saveCurrentUser(currentUser: CurrentUserDomain) {
        this[stringPreferencesKey(CURRENT_USER)] = gson.toJson(currentUser)
    }

    private fun Preferences.getCurrentUserValue(): CurrentUserDomain =
        getCurrentUser() ?: currentUserNull

    companion object {
        private const val CURRENT_USER = "CURRENT_USER"

        private val currentUserNull = CurrentUserDomain(
            id = -1,
            auth = "",
            email = ""
        )
    }
}
