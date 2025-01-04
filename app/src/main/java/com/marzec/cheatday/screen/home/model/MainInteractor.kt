package com.marzec.cheatday.screen.home.model

import com.marzec.cheatday.repository.FeatureToggleRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.content.Content
import com.marzec.content.mapData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class MainInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val featureToggleDataSource: FeatureToggleRepository
) {

    suspend fun observeMainState(): Flow<Pair<Boolean, Boolean>> =
        combine(listOf(observeIfUserLogged(), observeCounterFeatureFlag())) {
            it.first() to it[1]

        }

    private suspend fun observeCounterFeatureFlag(): Flow<Boolean> =
        featureToggleDataSource.observeAll()
            .map { content ->
                (content as? Content.Data)
                    ?.data
                    ?.firstOrNull { it.name == FEATURE_FLAG_COUNTER }
                    ?.value?.toBoolean() == true
            }

    private fun observeIfUserLogged(): Flow<Boolean> = userRepository.observeIfUserLogged()

    companion object {
        const val FEATURE_FLAG_COUNTER = "cheatDay.counter"
    }
}
