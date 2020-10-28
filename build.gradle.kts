// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val kotlin_version by extra("1.4.10")
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(Config.BuildPlugins.androidBuildPlugin)
        classpath(Config.BuildPlugins.kotlinGradlePlugin)
        classpath(Config.BuildPlugins.jUnit5Plugin)
        classpath(Config.BuildPlugins.kotlinAllOpen)
        classpath(Config.BuildPlugins.screenshots)
        classpath(Config.BuildPlugins.navigationSafeArgs)
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

gradle.projectsEvaluated {
    tasks.withType<JavaCompile> {
        val compilerArgs = options.compilerArgs
        compilerArgs.add("-Xmaxerrs")
        compilerArgs.add("500")
    }
}

tasks.create<Delete>("clean") {
    delete = setOf(
        rootProject.buildDir
    )
}