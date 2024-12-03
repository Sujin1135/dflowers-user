plugins {
    alias(libs.plugins.jooq)
}

dependencies {
    implementation(project(":subproject:domain"))

    implementation(libs.bundles.db)
}
