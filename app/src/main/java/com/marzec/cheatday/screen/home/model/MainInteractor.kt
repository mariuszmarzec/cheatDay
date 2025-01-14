package com.marzec.cheatday.screen.home.model

import com.marzec.cheatday.repository.UserRepository
import com.marzec.featuretoggle.FeatureTogglesManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MainInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val featureTogglesManager: FeatureTogglesManager
) {

    fun observeMainState(): Flow<Pair<Boolean, Boolean>> =
        combine(listOf(observeIfUserLogged(), observeCounterFeatureFlag())) {
            it.first() to it[1]
        }

    private fun observeCounterFeatureFlag(): Flow<Boolean> =
        featureTogglesManager.observe(FEATURE_FLAG_COUNTER)

    private fun observeIfUserLogged(): Flow<Boolean> = userRepository.observeIfUserLogged()

    companion object {
        const val FEATURE_FLAG_COUNTER = "cheatDay.counter"
    }
}
