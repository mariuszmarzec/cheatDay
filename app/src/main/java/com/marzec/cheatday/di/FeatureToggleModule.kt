package com.marzec.cheatday.di

import android.content.Context
import com.google.gson.Gson
import com.marzec.cache.Cache
import com.marzec.cache.ManyItemsCacheSaver
import com.marzec.cache.MemoryCache
import com.marzec.cache.MemoryListCacheSaver
import com.marzec.cheatday.api.FeatureToggleDataSource
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.common.FeatureTogglesManagerInitializerImpl
import com.marzec.featuretoggle.FeatureToggle
import com.marzec.featuretoggle.FeatureToggleRepository
import com.marzec.featuretoggle.FeatureTogglesManager
import com.marzec.featuretoggle.toDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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

    @Provides
    fun provideFeatureTogglesManager(
        @ApplicationContext context: Context,
        gson: Gson,
        featureToggleRepository: FeatureToggleRepository,
        @Named(AppModule.UPDATER_COROUTINE_SCOPE) updaterCoroutineScope: CoroutineScope,
    ): FeatureTogglesManager =
        FeatureTogglesManagerInitializerImpl(
            context,
            gson,
            featureToggleRepository,
            MemoryCache(),
            updaterCoroutineScope,
            FEATURE_TOGGLE_CONF_FILE
        ).create()

    private companion object {
        const val FEATURE_TOGGLE_MEMORY_CACHE_KEY = "FEATURE_TOGGLE_MEMORY_CACHE_KEY"
        const val FEATURE_TOGGLE_CONF_FILE = "feature_toggle_conf.json"
    }
}
