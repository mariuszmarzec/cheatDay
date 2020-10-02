object Config {

    val kotlin_version = "1.3.72"
    val junit4_version = "4.13"
    val leak_canary_version = "2.5"
    val coroutines_version = "1.3.7"
    val dagger_version = "2.27"
    val appcompat_version = "1.1.0"
    val material_version = "1.1.0"
    val ktx_version = "2.3.0-alpha02"
    val core_ktx_version = "1.2.0"
    val constraint_layout_version = "1.1.3"
    val retrofit_version = "2.6.0"
    val rxjava_version = "2.2.19"
    val gson_version = "2.8.5"
    val okhttp_version = "4.0.0"
    val rxandroid_version = "2.1.1"
    val anko_version = "0.10.8"
    val junit5_version = "5.5.0"
    val mockito_version = "3.3.3"
    val mockito_kotlin_version = "2.1.0"
    val joda_version = "2.10.3"
    val viewmodel_version = "1.1.1"
    val livedata_version = "2.2.0-rc03"
    val room_version = "2.2.5"
    val android_test_version = "1.1.1"
    val android_test_runner_version = "1.1.0"
    val paris_version = "1.3.0"
    val arch_testing_version = "2.1.0"
    val rx_preferences_version = "2.0.0"
    val preferences_ktx_version = "1.1.1"
    val reactivestreams_version = "1.1.1"
    val epoxy_version = "3.11.0"
    val espresso_core = "3.2.0"
    val idler_version = "0.10.0"
    val fragment_version = "1.2.5"
    val activity_version = "1.1.0"
    val screenshot_testing_version = "0.13.0"
    val kakao_version = "2.3.4"
    val nav_version = "2.3.0"
    val junit5_plugin_version = "1.4.2.1"
    val android_gradle_plugin = "4.0.1"

    object BuildPlugins {
        val androidBuildPlugin = "com.android.tools.build:gradle:${android_gradle_plugin}"
        val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}"
        val jUnit5Plugin = "de.mannodermaus.gradle.plugins:android-junit5:${junit5_plugin_version}"
        val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${kotlin_version}"
        val screenshots = "com.facebook.testing.screenshot:plugin:${screenshot_testing_version}"
        val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${nav_version}"
    }

    object Android {
        val compileSdkVersion = 29
        val applicationId = "com.marzec.cheatday"
        val minSdkVersion = 29
        val targetSdkVersion = 29
        val versionCode = 1
        val versionName = "1.0"
    }
    object Libs
    object TestLibs
}