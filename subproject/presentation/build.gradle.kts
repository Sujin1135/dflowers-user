plugins {
    id("java-library")
}

dependencies {
    implementation(project(":subproject:interface"))

    implementation("io.grpc:grpc-stub:1.68.1")
}
