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
    fun provideRemoteWeightDataSource(
        @ApiHost apiHost: String,
        weightRoomDataSource: Provider<WeightRoomDataSource>,
        featureToggleDataSourceImpl: Provider<WeightDataSourceImpl>
    ): WeightDataSource =
        if (apiHost == Api.LOCALHOST_API) {
            weightRoomDataSource.get()
        } else {
            featureToggleDataSourceImpl.get()
        }

    @Provides
    @Named(FEATURE_TOGGLE_MEMORY_CACHE_SAVER)
    fun provideMemoryCacheSaver(cache: Cache): ManyItemsCacheSaver<Int, FeatureToggle> =
        MemoryListCacheSaver(
            key = FEATURE_TOGGLE_MEMORY_CACHE_KEY,
            memoryCache = cache,
            isSameId = { id == it }
        )

    @Provides
    @Named(FEATURE_TOGGLE_ROOM_CACHE_SAVER)
    fun provideWeightRoomSaver(
        dao: WeightDao,
        userRepository: UserRepository
    ): ManyItemsCacheSaver<Long, FeatureToggle> =
        WeightRoomSaver(dao, userRepository)

    @Provides
    @Named(FEATURE_TOGGLE_CACHE_SAVER)
    fun provideWeightManyItemsCacheSaver(
        @Named(FEATURE_TOGGLE_MEMORY_CACHE_SAVER) memoryListCacheSaver: ManyItemsCacheSaver<Long, FeatureToggle>,
        @Named(FEATURE_TOGGLE_ROOM_CACHE_SAVER) roomCacheSaver: ManyItemsCacheSaver<Long, FeatureToggle>,
    ): ManyItemsCacheSaver<Long, FeatureToggle> = CompositeManyItemsCacheSaver(
        savers = listOf(
            memoryListCacheSaver,
            roomCacheSaver
        )
    )

    @Provides
    @Named(FEATURE_TOGGLE_REMOTE_REPOSITORY)
    fun provideFeatureToggleRemoteRepository(
        featureToggleDataSource: WeightDataSource,
        coroutineDispatcher: CoroutineDispatcher,
        @Named(FEATURE_TOGGLE_CACHE_SAVER) cacheSaver: ManyItemsCacheSaver<Long, FeatureToggle>,
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

    @Provides
    @Named(FEATURE_TOGGLE_ONLY_MEMORY_REPOSITORY)
    fun provideFeatureToggleLocalRepository(
        featureToggleDataSource: FeatureToggleDataSource,
        coroutineDispatcher: CoroutineDispatcher,
        @Named(FEATURE_TOGGLE_MEMORY_CACHE_SAVER) cacheSaver: ManyItemsCacheSaver<Int, FeatureToggle>,
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

    @Provides
    fun provideFeatureToggleRepository(
        @Named(FEATURE_TOGGLE_REMOTE_REPOSITORY) remoteRepository: Provider<FeatureToggleRepository>,
        @Named(FEATURE_TOGGLE_ONLY_MEMORY_REPOSITORY) localRepository: Provider<FeatureToggleRepository>,
        @ApiHost apiHost: String
    ): FeatureToggleRepository = if (apiHost == Api.LOCALHOST_API) {
        localRepository.get()
    } else {
        remoteRepository.get()
    }

    private companion object {
        const val FEATURE_TOGGLE_MEMORY_CACHE_KEY = "FEATURE_TOGGLE_MEMORY_CACHE_KEY"

        const val FEATURE_TOGGLE_MEMORY_CACHE_SAVER = "FEATURE_TOGGLE_MEMORY_CACHE_SAVER"
        const val FEATURE_TOGGLE_ROOM_CACHE_SAVER = "FEATURE_TOGGLE_ROOM_CACHE_SAVER"
        const val FEATURE_TOGGLE_CACHE_SAVER = "FEATURE_TOGGLE_CACHE_SAVER"

        const val FEATURE_TOGGLE_REMOTE_REPOSITORY = "FEATURE_TOGGLE_REMOTE_REPOSITORY"
        const val FEATURE_TOGGLE_ONLY_MEMORY_REPOSITORY = "FEATURE_TOGGLE_ONLY_MEMORY_REPOSITORY"
    }
}