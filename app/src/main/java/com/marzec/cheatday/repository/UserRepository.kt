package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.extensions.EMPTY_STRING
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.model.domain.CurrentUserProto
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.model.domain.toDomain
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val currentUserStore: DataStore<CurrentUserProto>,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getCurrentUser(): User = withContext(dispatcher) {
        val currentUserEmail = currentUserStore.data.first().email
        userDao.getUser(currentUserEmail).toDomain()
    }

    suspend fun getCurrentUserWithAuthToken(): CurrentUserDomain? = withContext(dispatcher) {
        currentUserStore.data.first()
            .takeIf { it.email.isNotEmpty() }
            ?.let { user ->
                CurrentUserDomain(id = user.id, auth = user.authToken, email = user.email)
            }
    }

    fun observeCurrentUser(): Flow<User> {
        return currentUserStore.data.flatMapMerge { currentUser ->
            userDao.observeUser(currentUser.email).filterNotNull().map { it.toDomain() }
        }.flowOn(dispatcher)
    }

    fun observeIfUserLogged(): Flow<Boolean> =
        currentUserStore.data.map { it.email.isNotEmpty() && it.authToken.isNotEmpty() }

    suspend fun clearCurrentUser(): Unit = withContext(dispatcher) {
        currentUserStore.updateData {
            it.toBuilder()
                .setAuthToken(EMPTY_STRING)
                .setEmail(EMPTY_STRING)
                .setId(-1)
                .build()
        }
    }

    suspend fun addUserToDbIfNeeded(user: User) {
        addUserToDbIfNeeded(user.email)
    }

    private suspend fun addUserToDbIfNeeded(email: String) {
        val userExist = userDao.observeUser(email).firstOrNull() != null
        if (!userExist) {
            userDao.insert(UserEntity(0, email))
        }
    }

    suspend fun setCurrentUserWithAuth(newUser: CurrentUserDomain): Unit = withContext(dispatcher) {
        addUserToDbIfNeeded(newUser.email)
        currentUserStore.updateData {
            it.toBuilder()
                .setAuthToken(newUser.auth)
                .setEmail(newUser.email)
                .setId(newUser.id)
                .build()
        }
        Unit
    }
}
