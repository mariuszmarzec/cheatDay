import java.util.Properties
import java.io.FileInputStream

fun readProperties(): Properties {
    val properties = Properties()
    val propertiesFile = File("${rootDir}/local.properties")
    if (propertiesFile.exists()) {
        properties.load(FileInputStream(propertiesFile))
    }
    return properties
}

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("de.mannodermaus.android-junit5")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    jacoco
    id("app.cash.paparazzi")
    id("io.gitlab.arturbosch.detekt")
    id("com.google.devtools.ksp")
}

jacoco {
    toolVersion = libs.versions.jacoco.get().toString()
}
apply(from = "jacoco.gradle.kts")

android {
    namespace = "com.marzec.cheatday"

    compileSdkVersion(libs.versions.compileSdk.get().toInt())
    defaultConfig {
        applicationId = "com.marzec.cheatday"
        minSdkVersion(libs.versions.minSdk.get().toInt())
        targetSdkVersion(libs.versions.targetSdk.get().toInt())
        versionCode = 1
        versionName = "1.0"
        testApplicationId = "com.marzec.cheatday.debug.test"
        testInstrumentationRunner = "com.marzec.cheatday.MockTestRunner"
        // Using orchestrator is not possible, because project is affected by
        // https://stackoverflow.com/questions/75257759/unknown-block-type-reading-jacoco-execution-data-files-from-android
        // https://issuetracker.google.com/issues/266538654
//        testInstrumentationRunnerArguments.putAll(
//            mapOf(
//                "clearPackageData" to "true",
//                "useTestStorageService" to "true"
//            )
//        )

        javaCompileOptions {
            annotationProcessorOptions {

                argument("room.schemaLocation", "$projectDir/schemas")
                argument("room.incremental", "true")
                argument("room.expandProjection", "true")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            jvmTarget = "17"
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    signingConfigs {
        create("release") {
            val properties = readProperties()
            storeFile = file(properties.getProperty("storeFile"))
            keyAlias = properties.getProperty("keyAlias")
            storePassword = properties.getProperty("storePassword")
            keyPassword = properties.getProperty("keyPassword")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions("api")

    productFlavors {
        val properties = readProperties()
        val prodApiUrl = properties.getProperty("prod.apiUrl")
        val prodAuthHeader = properties.getProperty("prod.authHeader")
        val testApiUrl = properties.getProperty("test.apiUrl")
        val testAuthHeader = properties.getProperty("test.authHeader")


        create("local") {
            buildConfigField("String", "HOST", "\"http://localhost\"")
            buildConfigField("String", "AUTHORIZATION", "\"LOCAL_AUTHORIZATION\"")
            setDimension("api")
        }

        create("stage") {
            buildConfigField("String", "HOST", "\"$testApiUrl\"")
            buildConfigField("String", "AUTHORIZATION", "\"$testAuthHeader\"")
            setDimension("api")
        }

        create("prod") {
            buildConfigField("String", "HOST", "\"$prodApiUrl\"")
            buildConfigField("String", "AUTHORIZATION", "\"$prodAuthHeader\"")
            setDimension("api")
        }
    }

    testOptions {
        // Using orchestrator commented out due to jacoco issue https://issuetracker.google.com/issues/266538654
        // execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    testCoverage {
        jacocoVersion = libs.versions.jacoco.get().toString()
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

hilt {
    enableTransformForLocalTests = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    detektPlugins(libs.detekt.formatting)

    // leakcanary
    debugImplementation(libs.leakCanary)
    androidTestImplementation(libs.leakCanaryInstrumentation)

    // kotlin
    implementation(libs.kotlinStdLib)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.coroutines.test)

    // androidx
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.viewmodel)
    implementation(libs.viewmodel.ktx)
    implementation(libs.lifecycle.ktx)
    implementation(libs.material)
    implementation(libs.fragment)
    debugImplementation(libs.fragment.testing)
    implementation(libs.activity)
    androidTestImplementation(libs.archCore.testing)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.common)
    ksp(libs.room.compiler)
    androidTestImplementation(libs.room.testing)

    // android views
    implementation(libs.constraintLayout)
    implementation(libs.recyclerview)

    // http client
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.mockWebServer)

    // joda time
    implementation(libs.joda.android)
    testImplementation(libs.joda)

    // navigation
    implementation(libs.nav.fragment.ktx)
    implementation(libs.nav.ui.ktx)
    implementation(libs.nav.dynamic.features)
    androidTestImplementation(libs.nav.testing)

    // charts
    implementation(libs.chart)

    // data store
    implementation(libs.datastore.preferences)
    implementation(libs.datastore)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)

    // hilt
    implementation(libs.hilt.android)
    debugImplementation(libs.hilt.android.testing)
    ksp(libs.hilt.compiler)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android)

    // junit4
    testImplementation(libs.junit4)
    testRuntimeOnly(libs.jUnit5.vintage)

    androidTestImplementation(libs.android.junit.ext)
    androidTestImplementation(libs.junit4)

    // test5
    testImplementation(libs.jUnit5.api)
    testRuntimeOnly(libs.jUnit5.engine)
    // mocking
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)

    // truth
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    // test runner
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.android.test.rules)
    androidTestUtil(libs.android.test.orchestrator)
    androidTestUtil(libs.android.test.services)
    androidTestImplementation(libs.fragment)

    // espresso
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)

    // kaspresso
    androidTestImplementation(libs.kaspresso)

    androidTestImplementation(libs.hamcrest)
}

detekt {
    autoCorrect = true
    source = files(
        "src/androidTest/java",
        "src/main/java",
        "src/test/java"
    )

    config = files("../config/detekt/detekt.yml")
}

// workaround from https://github.com/cashapp/paparazzi/issues/955
// https://github.com/cashapp/paparazzi/issues?q=%22attempted+to+delete+a+method%22
tasks.withType<Test> {
    jvmArgs("-XX:+AllowRedefinitionToAddDeleteMethods")
}