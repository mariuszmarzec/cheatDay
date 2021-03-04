package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import com.marzec.cheatday.common.Constants.DEFAULT_USER
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.extensions.emptyString
import com.marzec.cheatday.model.domain.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val currentUser: DataStore<CurrentUserProto>
) : UserRepository {

    override suspend fun getUserByEmail(email: String): User =
        userDao.getUser(email).let(UserEntity::toDomain)

    override fun getUserByEmailFlow(email: String): Flow<User> {
        return userDao.observeUser(email).map { it.toDomain() }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserByEmailSuspend(email: String): User = withContext(Dispatchers.IO) {
        userDao.getUser(email).toDomain()
    }

    override suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
        val currentUserEmail = currentUser.data.first().email
        getUserByEmail(currentUserEmail.ifEmpty { DEFAULT_USER })
    }

    override suspend fun getCurrentUserWithAuth(): CurrentUserDomain? = withContext(Dispatchers.IO) {
        val user = currentUser.data.first()
        if (user.email.isNotEmpty()) {
            CurrentUserDomain(user.id, user.email, user.authToken)
        } else {
            null
        }
    }

    @FlowPreview
    override fun getCurrentUserFlow(): Flow<User> {
        return currentUser.data.flatMapMerge { currentUser ->
            getUserByEmailFlow(currentUser.email.ifEmpty { DEFAULT_USER })
        }
    }

    @Deprecated("deprecated", replaceWith = ReplaceWith("Use getCurrentUser"))
    override suspend fun getCurrentUserSuspend(): User = getCurrentUser()

    override fun observeIfUserLogged(): Flow<Boolean> {
        return currentUser.data.map { it.email.isNotEmpty() && it.authToken.isNotEmpty() }
    }

    override suspend fun clearCurrentUser() {
        setCurrentUserWithAuth(CurrentUserDomain(-1, emptyString(), emptyString()))
    }

    override suspend fun setCurrentUserWithAuth(newUser: CurrentUserDomain) = withContext(Dispatchers.IO) {
        currentUser.updateData {
            it.toBuilder()
                .setAuthToken(newUser.auth)
                .setEmail(newUser.email)
                .setId(newUser.id)
                .build()
        }
        Unit
    }
}

interface UserRepository {

    suspend fun getUserByEmail(email: String): User

    fun getUserByEmailFlow(email: String): Flow<User>

    suspend fun getCurrentUser(): User

    suspend fun getCurrentUserWithAuth(): CurrentUserDomain?

    suspend fun setCurrentUserWithAuth(newUser: CurrentUserDomain)

    fun getCurrentUserFlow(): Flow<User>

    suspend fun getUserByEmailSuspend(email: String): User

    suspend fun getCurrentUserSuspend(): User

    fun observeIfUserLogged() : Flow<Boolean>

    suspend fun clearCurrentUser()
}