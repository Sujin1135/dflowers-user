dependencies {
    implementation(project(":subproject:domain"))

    implementation(libs.bundles.jjwt)
    implementation(libs.bundles.crypto)

    testImplementation(project(":subproject:infrastructure"))
    testImplementation(testFixtures(project(":subproject:domain")))

    testImplementation(libs.bundles.testcontainers)
}
