dependencies {
    implementation(project(":subproject:interface"))

    implementation(libs.grpc.spring)
    implementation(libs.grpc.services)
}
