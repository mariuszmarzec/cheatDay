package com.marzec.cheatday.common

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marzec.cache.MemoryCache
import com.marzec.cheatday.extensions.readFile
import com.marzec.featuretoggle.FeatureToggleRepository
import com.marzec.featuretoggle.FeatureTogglesManager
import com.marzec.featuretoggle.FeatureTogglesManagerImpl
import com.marzec.featuretoggle.FeatureTogglesManagerInitializer
import kotlinx.coroutines.CoroutineScope

class FeatureTogglesManagerInitializerImpl(
    private val context: Context,
    private val gson: Gson,
    private val featureToggleRepository: FeatureToggleRepository,
    private val memoryCache: MemoryCache,
    private val updaterCoroutineScope: CoroutineScope,
    private val featureToggleConfFile: String
) : FeatureTogglesManagerInitializer {

    override fun create(): FeatureTogglesManager {
        val featureTogglesDefaults: Map<String, String> = context.readFile(featureToggleConfFile)
            .let {
                val type = object : TypeToken<Map<String, String>>() {}.type
                gson.fromJson(it, type)
            }
        return FeatureTogglesManagerImpl(
            featureTogglesDefaults = featureTogglesDefaults,
            featureToggleRepository = featureToggleRepository,
            memoryCache = memoryCache,
            updaterCoroutineScope = updaterCoroutineScope
        ).apply { init() }
    }
}
