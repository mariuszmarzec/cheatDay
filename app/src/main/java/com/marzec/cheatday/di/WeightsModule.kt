package com.marzec.cheatday.di

import com.marzec.cache.Cache
import com.marzec.cache.ManyItemsCacheSaver
import com.marzec.cache.MemoryListCacheSaver
import com.marzec.cache.sortByInserter
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.WeightDataSource
import com.marzec.cheatday.api.WeightDataSourceImpl
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toCreateDto
import com.marzec.cheatday.model.domain.toDto
import com.marzec.cheatday.repository.WeightCrudRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
class WeightsModule {

    @Provides
    fun provideWeightDataSource(api: WeightApi): WeightDataSource = WeightDataSourceImpl(api)

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
    @Named(WEIGHTS_CACHE_SAVER)
    fun provideWeightManyItemsCacheSaver(
        @Named(WEIGHTS_MEMORY_CACHE_SAVER) memoryListCacheSaver: ManyItemsCacheSaver<Long, WeightResult>
    ): ManyItemsCacheSaver<Long, WeightResult> = memoryListCacheSaver

    @Provides
    fun provideWeightCrudRepository(
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

    companion object {
        const val WEIGHTS_MEMORY_CACHE_KEY = "WEIGHTS_MEMORY_CACHE_KEY"
        const val WEIGHTS_MEMORY_CACHE_SAVER = "WEIGHTS_MEMORY_CACHE_SAVER"
        const val WEIGHTS_CACHE_SAVER = "WEIGHTS_CACHE_SAVER"
    }
}