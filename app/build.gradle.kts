import java.util.Properties
import java.io.FileInputStream
import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.compiler.plugin.parsePluginOption

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("de.mannodermaus.android-junit5")
    id("com.facebook.testing.screenshot")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.protobuf")
    id("dagger.hilt.android.plugin")
    jacoco
}

jacoco {
    toolVersion = "0.8.7"
}

apply(from = "jacoco.gradle.kts")

android {
    compileSdkVersion(Dependency.Android.compileSdkVersion)
    defaultConfig {
        applicationId = Dependency.Android.applicationId
        minSdkVersion(Dependency.Android.minSdkVersion)
        targetSdkVersion(Dependency.Android.targetSdkVersion)
        versionCode = Dependency.Android.versionCode
        versionName = Dependency.Android.versionName
        testInstrumentationRunner = "com.marzec.cheatday.MockTestRunner"
        testInstrumentationRunnerArgument("listener", "leakcanary.FailTestOnLeakRunListener")

        javaCompileOptions {
            annotationProcessorOptions {

                argument("room.schemaLocation", "$projectDir/schemas")
                argument("room.incremental", "true")
                argument("room.expandProjection", "true")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    signingConfigs {
        create("release") {
            val properties = Properties()
            val propertiesFile = File("local.properties")
            if (propertiesFile.exists()) {
                properties.load(FileInputStream(propertiesFile))
                storeFile = file(properties.getProperty("storeFile"))
                keyAlias = properties.getProperty("keyAlias")
                storePassword = properties.getProperty("storePassword")
                keyPassword = properties.getProperty("keyPassword")
            }
        }
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
            isTestCoverageEnabled = true
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
        create("stage") {
            buildConfigField("String", "HOST", "\"http://fiteo-env-1.eba-cba76vkj.us-east-2.elasticbeanstalk.com/test\"")
            buildConfigField("String", "AUTHORIZATION", "\"Authorization-Test\"")
            setDimension("api")
        }

        create("prod") {
            buildConfigField("String", "HOST", "\"http://fiteo-env-1.eba-cba76vkj.us-east-2.elasticbeanstalk.com\"")
            buildConfigField("String", "AUTHORIZATION", "\"Authorization\"")
            setDimension("api")
        }
    }

    testOptions.animationsDisabled = true
    testOptions.unitTests.isIncludeAndroidResources = true

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

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // leakcanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependency.leak_canary_version}")
    androidTestImplementation("com.squareup.leakcanary:leakcanary-android-instrumentation:${Dependency.leak_canary_version}")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Dependency.kotlin_version}")
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
    implementation("androidx.lifecycle:lifecycle-livedata:${Dependency.livedata_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependency.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Dependency.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Dependency.ktx_version}")
    implementation("com.google.android.material:material:${Dependency.material_version}")
    implementation("androidx.fragment:fragment:${Dependency.fragment_version}")
    debugImplementation("androidx.fragment:fragment-testing:${Dependency.fragment_version}")
    implementation("androidx.activity:activity:${Dependency.activity_version}")
    androidTestImplementation("androidx.test:rules:${Dependency.android_test_version}")
    androidTestImplementation("androidx.arch.core:core-testing:${Dependency.arch_testing_version}")

    // room
    implementation("androidx.room:room-runtime:${Dependency.room_version}")
    implementation("androidx.room:room-ktx:${Dependency.room_version}")
    kapt("androidx.room:room-compiler:${Dependency.room_version}")
    androidTestImplementation("androidx.room:room-testing:${Dependency.room_version}")

    // android views
    implementation("androidx.constraintlayout:constraintlayout:${Dependency.constraint_layout_version}")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    // http client
    implementation("com.squareup.retrofit2:retrofit:${Dependency.retrofit_version}")
    implementation("com.google.code.gson:gson:${Dependency.gson_version}")
    implementation("com.squareup.okhttp3:okhttp:${Dependency.okhttp_version}")
    implementation("com.squareup.retrofit2:converter-gson:${Dependency.retrofit_version}")

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
    androidTestImplementation("org.mockito:mockito-android:${Dependency.mockito_version}")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependency.mockito_kotlin_version}")
    testImplementation("org.mockito:mockito-core:${Dependency.mockito_version}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependency.mockito_kotlin_version}")
    testImplementation("io.mockk:mockk:${Dependency.mockk_version}")
    testImplementation("io.mockk:mockk-agent-jvm:${Dependency.mockk_version}")

    // truth
    testImplementation("com.google.truth:truth:${Dependency.truth_version}")
    androidTestImplementation("com.google.truth:truth:${Dependency.truth_version}")

    // test runner
    androidTestImplementation("androidx.test:runner:${Dependency.android_test_version}")
    androidTestImplementation("androidx.fragment:fragment-testing:${Dependency.fragment_version}")

    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:${Dependency.espresso_core}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Dependency.espresso_core}")

    // kakao
    androidTestImplementation("com.agoda.kakao:kakao:${Dependency.kakao_version}")
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