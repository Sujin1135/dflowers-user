dependencies {
    implementation(project(":subproject:interface"))
    implementation(project(":subproject:application"))
    implementation(project(":subproject:domain"))

    implementation(libs.grpc.spring)
    implementation(libs.grpc.services)
    implementation(libs.spring.security)
    implementation(libs.swagger)
    implementation(libs.spring.validation)
}
