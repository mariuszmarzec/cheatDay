
val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/stageDebug") {
    exclude(
        "**/com/paris/**",
        "**/com/airbnb/paris/extensions/**"
    )
}

val kotlinSrc = "${project.projectDir}/src/main/java"

tasks.create("jacocoTestReportStageDebug", JacocoReport::class) {
    dependsOn("createStageDebugCoverageReport")
    dependsOn("testStageDebugUnitTest")

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }

    classDirectories.setFrom(files(kotlinTree))

    additionalSourceDirs.setFrom(files(kotlinSrc))
    sourceDirectories.setFrom(files(kotlinSrc))
    executionData.setFrom(
        fileTree(
            baseDir = project.projectDir
        ) {
            include(
                "**/*.exec",
                "**/*.ec"
            )
        }
    )
}
