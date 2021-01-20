package com.marzec.cheatday.repository

import com.marzec.cheatday.common.Constants.DEFAULT_USER
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.model.domain.toDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
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
        getUserByEmail(DEFAULT_USER)
    }

    override fun getCurrentUserFlow(): Flow<User> {
        return getUserByEmailFlow(DEFAULT_USER)
    }

    override suspend fun getCurrentUserSuspend(): User = withContext(Dispatchers.IO) {
        getUserByEmailSuspend(DEFAULT_USER)
    }
}

interface UserRepository {

    suspend fun getUserByEmail(email: String): User

    fun getUserByEmailFlow(email: String): Flow<User>

    suspend fun getCurrentUser(): User

    fun getCurrentUserFlow(): Flow<User>

    suspend fun getUserByEmailSuspend(email: String): User

    suspend fun getCurrentUserSuspend(): User
}