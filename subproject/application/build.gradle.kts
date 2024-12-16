dependencies {
    implementation(project(":subproject:domain"))

    testImplementation(project(":subproject:infrastructure"))

    testImplementation(libs.bundles.testcontainers)
}
