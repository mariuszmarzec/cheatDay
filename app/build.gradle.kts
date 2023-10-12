import java.util.Properties
import java.io.FileInputStream
import com.google.protobuf.gradle.*

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
    kotlin("kapt")
    id("kotlin-parcelize")
    id("de.mannodermaus.android-junit5")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.protobuf")
    id("dagger.hilt.android.plugin")
//    jacoco
    id("shot")
    id("io.gitlab.arturbosch.detekt")
    id("com.jakewharton.butterknife")
}

//jacoco {
//    toolVersion = "0.8.7"
//}

apply(from = "jacoco.gradle.kts")

android {
    namespace = "com.marzec.cheatday"

    compileSdkVersion(Dependency.Android.compileSdkVersion)
    defaultConfig {
        applicationId = Dependency.Android.applicationId
        minSdkVersion(Dependency.Android.minSdkVersion)
        targetSdkVersion(Dependency.Android.targetSdkVersion)
        versionCode = Dependency.Android.versionCode
        versionName = Dependency.Android.versionName
        testInstrumentationRunner = "com.marzec.cheatday.MockTestRunner"
        testInstrumentationRunnerArguments.putAll(
            mapOf(
                "listener" to "leakcanary.FailTestOnLeakRunListener",
                "clearPackageData" to "true",
                "coverage" to "true",
                "useTestStorageService" to "true"
            )
        )

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
//            enableUnitTestCoverage = true
//            enableAndroidTestCoverage = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isMinifyEnabled = false
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

    testOptions.animationsDisabled = true
    testOptions.unitTests.isReturnDefaultValues = true
    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions {
        // With enabled ANDROIDX_TEST_ORCHESTRATOR screenshot test with shot doesn't work
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    testCoverage {
        jacocoVersion = "0.8.7"
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

kapt {
    correctErrorTypes = true

    arguments {
        arg("dagger.experimentalDaggerErrorMessages", "enabled")
    }
}

hilt {
    enableTransformForLocalTests = true
}

shot {
    applicationId = "com.marzec.cheatday"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // leakcanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependency.leak_canary_version}")
    androidTestImplementation("com.squareup.leakcanary:leakcanary-android-instrumentation:${Dependency.leak_canary_version}")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Dependency.kotlin_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependency.coroutines_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependency.coroutines_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependency.coroutines_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependency.coroutines_version}")

    // anko
    implementation("org.jetbrains.anko:anko-commons:${Dependency.anko_version}")

    // androidx
    implementation("androidx.appcompat:appcompat:${Dependency.appcompat_version}")
    implementation("androidx.core:core-ktx:${Dependency.core_ktx_version}")
    implementation("android.arch.lifecycle:viewmodel:${Dependency.viewmodel_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependency.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Dependency.ktx_version}")
    implementation("com.google.android.material:material:${Dependency.material_version}")
    implementation("androidx.fragment:fragment:${Dependency.fragment_version}")
    debugImplementation("androidx.fragment:fragment-testing:${Dependency.fragment_version}")
    implementation("androidx.activity:activity:${Dependency.activity_version}")
    androidTestImplementation("androidx.arch.core:core-testing:${Dependency.arch_testing_version}")

    // room
    implementation("androidx.room:room-runtime:${Dependency.room_version}")
    implementation("androidx.room:room-ktx:${Dependency.room_version}")
    implementation("androidx.room:room-common:${Dependency.room_version}")
//    kapt("androidx.room:room-compiler:${Dependency.room_version}")
    androidTestImplementation("androidx.room:room-testing:${Dependency.room_version}")

    // android views
    implementation("androidx.constraintlayout:constraintlayout:${Dependency.constraint_layout_version}")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    // http client
    implementation("com.squareup.retrofit2:retrofit:${Dependency.retrofit_version}")
    implementation("com.google.code.gson:gson:${Dependency.gson_version}")
    implementation("com.squareup.okhttp3:okhttp:${Dependency.okhttp_version}")
    implementation("com.squareup.retrofit2:converter-gson:${Dependency.retrofit_version}")
    implementation("com.squareup.okhttp3:mockwebserver:${Dependency.okhttp_version}")

    // joda time
    implementation("net.danlew:android.joda:${Dependency.joda_version}")
    testImplementation("joda-time:joda-time:${Dependency.joda_version}")

    // paris
    implementation("com.airbnb.android:paris:${Dependency.paris_version}")
    kapt("com.airbnb.android:paris-processor:${Dependency.paris_version}")

    // epoxy
    implementation("com.airbnb.android:epoxy:${Dependency.epoxy_version}")
    kapt("com.airbnb.android:epoxy-processor:${Dependency.epoxy_version}")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Dependency.nav_version}")
    implementation("androidx.navigation:navigation-ui-ktx:${Dependency.nav_version}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Dependency.nav_version}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Dependency.nav_version}")

    // charts
    implementation("com.github.PhilJay:MPAndroidChart:${Dependency.chart_version}")

    // data store
    implementation("androidx.datastore:datastore-preferences:${Dependency.datastore_version}")
    implementation("androidx.datastore:datastore:${Dependency.datastore_version}")

    // protobuf
    implementation("com.google.protobuf:protobuf-lite:3.0.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:${Dependency.firebase_version}"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // hilt
    implementation("com.google.dagger:hilt-android:${Dependency.hilt_version}")
    debugImplementation("com.google.dagger:hilt-android-testing:${Dependency.hilt_version}")
    kapt("com.google.dagger:hilt-compiler:${Dependency.hilt_version}")

    androidTestImplementation("com.google.dagger:hilt-android-testing:${Dependency.hilt_version}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Dependency.hilt_version}")

    // junit4
    testImplementation("junit:junit:${Dependency.junit4_version}")
    androidTestImplementation("androidx.test.ext:junit:${Dependency.android_test_runner_version}")
    androidTestImplementation("junit:junit:${Dependency.junit4_version}")

    // test5
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Dependency.junit5_version}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Dependency.junit5_version}")

    // mocking
    testImplementation("io.mockk:mockk:${Dependency.mockk_version}")
    testImplementation("io.mockk:mockk-agent-jvm:${Dependency.mockk_version}")

    // truth
    testImplementation("com.google.truth:truth:${Dependency.truth_version}")
    androidTestImplementation("com.google.truth:truth:${Dependency.truth_version}")

    // test runner
    androidTestImplementation("androidx.test:core:${Dependency.runner_version}")
    androidTestImplementation("androidx.test:runner:${Dependency.runner_version}")
    androidTestImplementation("androidx.test:rules:${Dependency.runner_version}")
    androidTestUtil("androidx.test:orchestrator:${Dependency.orchestrator_version}")
    androidTestUtil("androidx.test.services:test-services:${Dependency.android_test_services_version}")
    androidTestImplementation("androidx.fragment:fragment-testing:${Dependency.fragment_version}")

    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:${Dependency.espresso_core}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Dependency.espresso_core}")

    // kaspresso
    androidTestImplementation("com.kaspersky.android-components:kaspresso:${Dependency.kaspresso_version}")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.0.0"
    }
    plugins {
        id("javalite") {
            artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("javalite") { }
            }
        }
    }
}

detekt {
    source = files(
        "src/androidTest/java",
        "src/main/java",
        "src/test/java"
    )

    config = files("../config/detekt/detekt.yml")
}
