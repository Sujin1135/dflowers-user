plugins {
    id("java-library")
}

dependencies {
    implementation(project(":subproject:interface"))

    implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
    implementation("io.grpc:grpc-services:1.68.1")
}
