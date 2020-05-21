package com.marzec.cheatday.repository

import com.marzec.cheatday.common.Constants.DEFAULT_USER
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.domain.User
import com.marzec.cheatday.domain.toDomain
import com.marzec.cheatday.extensions.onIo
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUserByEmail(email: String): Single<User> {
        return userDao.getUser(email).map(UserEntity::toDomain).onIo()
    }

    override fun getUserByEmailFlow(email: String): Flow<User> {
        return userDao.getUserFlow(email).map { it.toDomain() }
    }

    override suspend fun getUserByEmailSuspend(email: String): User {
        return userDao.getUserSuspend(email).toDomain()
    }

    override fun getCurrentUser(): Single<User> {
        return getUserByEmail(DEFAULT_USER)
    }

    override fun getCurrentUserFlow(): Flow<User> {
        return getUserByEmailFlow(DEFAULT_USER)
    }

    override suspend fun getCurrentUserSuspend(): User {
        return getUserByEmailSuspend(DEFAULT_USER)
    }
}

interface UserRepository {

    fun getUserByEmail(email: String): Single<User>

    fun getUserByEmailFlow(email: String): Flow<User>

    fun getCurrentUser(): Single<User>

    fun getCurrentUserFlow(): Flow<User>

    suspend fun getUserByEmailSuspend(email: String): User

    suspend fun getCurrentUserSuspend(): User
}