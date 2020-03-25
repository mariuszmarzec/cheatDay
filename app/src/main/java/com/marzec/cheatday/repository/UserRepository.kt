package com.marzec.cheatday.repository

import com.marzec.cheatday.common.Constants.DEFAULT_USER
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.domain.User
import com.marzec.cheatday.domain.toDomain
import com.marzec.cheatday.extensions.onIo
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUserByEmail(email: String): Single<User> {
        return userDao.getUser(email).map(UserEntity::toDomain).onIo()
    }

    override fun getCurrentUser(): Single<User> {
        return getUserByEmail(DEFAULT_USER)
    }
}

interface UserRepository {

    fun getUserByEmail(email: String) : Single<User>

    fun getCurrentUser() : Single<User>
}