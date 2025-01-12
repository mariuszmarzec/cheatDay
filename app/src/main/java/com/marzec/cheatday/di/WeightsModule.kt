package com.marzec.cheatday.di

import com.marzec.cache.Cache
import com.marzec.cache.CompositeManyItemsCacheSaver
import com.marzec.cache.ManyItemsCacheSaver
import com.marzec.cache.MemoryListCacheSaver
import com.marzec.cache.sortByInserter
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.WeightDataSource
import com.marzec.cheatday.api.WeightDataSourceImpl
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.db.cachesaver.WeightRoomSaver
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.datasource.WeightRoomDataSource
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toCreateDto
import com.marzec.featuretoggle.toDto
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.repository.WeightCrudRepository
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
class WeightsModule {

    @Provides
    fun provideRemoteWeightDataSource(
        @ApiHost apiHost: String,
        weightRoomDataSource: Provider<WeightRoomDataSource>,
        weightDataSourceImpl: Provider<WeightDataSourceImpl>
    ): WeightDataSource =
        if (apiHost == Api.LOCALHOST_API) {
            weightRoomDataSource.get()
        } else {
            weightDataSourceImpl.get()
        }

    @Provides
    @Named(WEIGHTS_MEMORY_CACHE_SAVER)
    fun provideMemoryCacheSaver(cache: Cache): ManyItemsCacheSaver<Long, WeightResult> =
        MemoryListCacheSaver(
            key = WEIGHTS_MEMORY_CACHE_KEY,
            memoryCache = cache,
            isSameId = { id == it },
            newItemInsert = sortByInserter(byDescending = true) { it.date }
        )

    @Provides
    @Named(WEIGHTS_ROOM_CACHE_SAVER)
    fun provideWeightRoomSaver(
        dao: WeightDao,
        userRepository: UserRepository
    ): ManyItemsCacheSaver<Long, WeightResult> =
        WeightRoomSaver(dao, userRepository)

    @Provides
    @Named(WEIGHTS_CACHE_SAVER)
    fun provideWeightManyItemsCacheSaver(
        @Named(WEIGHTS_MEMORY_CACHE_SAVER) memoryListCacheSaver: ManyItemsCacheSaver<Long, WeightResult>,
        @Named(WEIGHTS_ROOM_CACHE_SAVER) roomCacheSaver: ManyItemsCacheSaver<Long, WeightResult>,
    ): ManyItemsCacheSaver<Long, WeightResult> = CompositeManyItemsCacheSaver(
        savers = listOf(
            memoryListCacheSaver,
            roomCacheSaver
        )
    )

    @Provides
    @Named(WEIGHTS_REMOTE_REPOSITORY)
    fun provideWeightCrudRemoteRepository(
        weightDataSource: WeightDataSource,
        coroutineDispatcher: CoroutineDispatcher,
        @Named(WEIGHTS_CACHE_SAVER) cacheSaver: ManyItemsCacheSaver<Long, WeightResult>,
        @Named(AppModule.UPDATER_COROUTINE_SCOPE) updaterCoroutineScope: CoroutineScope
    ): WeightCrudRepository =
        WeightCrudRepository(
            dataSource = weightDataSource,
            dispatcher = coroutineDispatcher,
            cacheSaver = cacheSaver,
            toDomain = { toDomain() },
            updateToDto = { toDto() },
            createToDto = { toCreateDto() },
            updaterCoroutineScope = updaterCoroutineScope
        )

    @Provides
    @Named(WEIGHTS_ONLY_MEMORY_REPOSITORY)
    fun provideWeightCrudLocalRepository(
        weightDataSource: WeightDataSource,
        coroutineDispatcher: CoroutineDispatcher,
        @Named(WEIGHTS_MEMORY_CACHE_SAVER) cacheSaver: ManyItemsCacheSaver<Long, WeightResult>,
        @Named(AppModule.UPDATER_COROUTINE_SCOPE) updaterCoroutineScope: CoroutineScope
    ): WeightCrudRepository =
        WeightCrudRepository(
            dataSource = weightDataSource,
            dispatcher = coroutineDispatcher,
            cacheSaver = cacheSaver,
            toDomain = { toDomain() },
            updateToDto = { toDto() },
            createToDto = { toCreateDto() },
            updaterCoroutineScope = updaterCoroutineScope
        )

    @Provides
    fun provideWeightCrudRepository(
        @Named(WEIGHTS_REMOTE_REPOSITORY) remoteRepository: Provider<WeightCrudRepository>,
        @Named(WEIGHTS_ONLY_MEMORY_REPOSITORY) localRepository: Provider<WeightCrudRepository>,
        @ApiHost apiHost: String
    ): WeightCrudRepository = if (apiHost == Api.LOCALHOST_API) {
        localRepository.get()
    } else {
        remoteRepository.get()
    }

    private companion object {
        const val WEIGHTS_MEMORY_CACHE_KEY = "WEIGHTS_MEMORY_CACHE_KEY"

        const val WEIGHTS_MEMORY_CACHE_SAVER = "WEIGHTS_MEMORY_CACHE_SAVER"
        const val WEIGHTS_ROOM_CACHE_SAVER = "WEIGHTS_ROOM_CACHE_SAVER"
        const val WEIGHTS_CACHE_SAVER = "WEIGHTS_CACHE_SAVER"

        const val WEIGHTS_REMOTE_REPOSITORY = "WEIGHTS_REMOTE_REPOSITORY"
        const val WEIGHTS_ONLY_MEMORY_REPOSITORY = "WEIGHTS_ONLY_MEMORY_REPOSITORY"
    }
}
