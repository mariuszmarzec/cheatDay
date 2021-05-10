
val fileFilter = listOf<String>()
val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
    exclude(*excludes.toTypedArray())
}

tasks.create("jacocoUnitTestReport", JacocoReport::class) {
    dependsOn("testDebugUnitTest")
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
    }
}

tasks.create("jacocoMergeTestReport", JacocoMerge::class) {
    dependsOn("connectedDebugAndroidTest")
    dependsOn("jacocoUnitTestReport")

    executionData = fileTree(
        project.projectDir
    ) {
        include(
            "**/*UnitTest.exec",
            "**/*.ec"
        )
    }
}

tasks.create("jacocoTestReport", JacocoReport::class) {
    dependsOn("jacocoMergeTestReport")

    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
        html.destination = file("build/reports/jacoco/merged")
    }

    classDirectories.setFrom(files(kotlinTree))
    executionData.setFrom(
        fileTree(
            baseDir = project.projectDir
        ) {
            include(
                "**/jacocoMergeTestReport.exec"
            )
            exclude(
                "**/*UnitTest.exec",
                "**/*.ec"
            )
        }
    )
}