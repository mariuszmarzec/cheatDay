package com.marzec.cheatday.di

import com.marzec.cache.Cache
import com.marzec.cache.CompositeManyItemsCacheSaver
import com.marzec.cache.ManyItemsCacheSaver
import com.marzec.cache.MemoryListCacheSaver
import com.marzec.cache.sortByInserter
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.FeatureToggleDataSource
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.model.domain.FeatureToggle
import com.marzec.cheatday.model.domain.toCreateDto
import com.marzec.cheatday.model.domain.toDto
import com.marzec.cheatday.repository.FeatureToggleRepository
import com.marzec.cheatday.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Provider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
class FeatureToggleModule {

    @Provides
    fun provideMemoryCacheSaver(cache: Cache): ManyItemsCacheSaver<Int, FeatureToggle> =
        MemoryListCacheSaver(
            key = FEATURE_TOGGLE_MEMORY_CACHE_KEY,
            memoryCache = cache,
            isSameId = { id == it }
        )

    @Provides
    fun provideFeatureToggleRemoteRepository(
        featureToggleDataSource: FeatureToggleDataSource,
        coroutineDispatcher: CoroutineDispatcher,
        cacheSaver: ManyItemsCacheSaver<Int, FeatureToggle>,
        @Named(AppModule.UPDATER_COROUTINE_SCOPE) updaterCoroutineScope: CoroutineScope
    ): FeatureToggleRepository =
        FeatureToggleRepository(
            dataSource = featureToggleDataSource,
            dispatcher = coroutineDispatcher,
            cacheSaver = cacheSaver,
            toDomain = { toDomain() },
            updateToDto = { toDto() },
            createToDto = { toDto() },
            updaterCoroutineScope = updaterCoroutineScope
        )

    private companion object {
        const val FEATURE_TOGGLE_MEMORY_CACHE_KEY = "FEATURE_TOGGLE_MEMORY_CACHE_KEY"
    }
}