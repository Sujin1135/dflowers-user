dependencies {
    implementation(project(":subproject:domain"))

    implementation(libs.bundles.jjwt)

    testImplementation(project(":subproject:infrastructure"))

    testImplementation(libs.bundles.testcontainers)
}
