package com.marzec.cheatday.repository

import com.marzec.cheatday.TestSchedulersRule
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.model.db.UserEntity
import com.marzec.cheatday.model.domain.User
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSchedulersRule::class)
internal class UserRepositoryImplTest {

    val userDao: UserDao = mock()

    lateinit var repository: UserRepository

    @BeforeEach
    fun setUp() {
        whenever(userDao.getUser(any())).thenReturn(Single.just(UserEntity("uuid", "email")))
        repository = UserRepositoryImpl(userDao)
    }

    @Test
    fun getUserByEmail() {
        repository.getUserByEmail("email")
            .test()
            .assertValue(User("uuid", "email"))
    }
}