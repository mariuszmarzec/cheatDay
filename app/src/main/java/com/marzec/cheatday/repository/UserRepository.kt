package com.marzec.cheatday.repository

import androidx.datastore.core.DataStore
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.extensions.emptyString
import com.marzec.cheatday.model.domain.*
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val currentUser: DataStore<CurrentUserProto>
) : UserRepository {

    override suspend fun getUserByEmail(email: String): User =
        userDao.getUser(email).let(UserEntity::toDomain)

    override fun getUserByEmailFlow(email: String): Flow<User> {
        return userDao.observeUser(email).filterNotNull().map { it.toDomain() }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserByEmailSuspend(email: String): User = withContext(Dispatchers.IO) {
        userDao.getUser(email).toDomain()
    }

    override suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
        val currentUserEmail = currentUser.data.first().email
        getUserByEmail(currentUserEmail)
    }

    override suspend fun getCurrentUserWithAuth(): CurrentUserDomain? = withContext(Dispatchers.IO) {
        val user = currentUser.data.first()
        if (user.email.isNotEmpty()) {
            CurrentUserDomain(id = user.id, auth = user.authToken, email = user.email)
        } else {
            null
        }
    }

    @FlowPreview
    override fun getCurrentUserFlow(): Flow<User> {
        return currentUser.data.flatMapMerge { currentUser ->
            getUserByEmailFlow(currentUser.email)
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

    override suspend fun addUserToDbIfNeeded(user: User) {
        val userExist = userDao.observeUser(user.email).firstOrNull() != null
        if (!userExist) {
            userDao.insert(UserEntity(UUID.randomUUID().toString(), user.email))
        }
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

    suspend fun addUserToDbIfNeeded(user: User)
}