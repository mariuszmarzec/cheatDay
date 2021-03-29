package com.marzec.cheatday.screen.home.model

import com.marzec.cheatday.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val userRepository: UserRepository
) {

    fun observeIfUserLogged(): Flow<Boolean> = userRepository.observeIfUserLogged()
}