package com.marzec.cheatday.feature.home

import com.marzec.cheatday.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val userRepository: UserRepository
) {

    fun observeIfUserLogged(): Flow<Boolean> = userRepository.observeIfUserLogged()
}