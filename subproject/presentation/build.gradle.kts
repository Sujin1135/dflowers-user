plugins {
    id("java-library")
}

allprojects {
    dependencies {
        implementation("io.grpc:grpc-stub:1.68.1")
        implementation("io.grpc:grpc-kotlin-stub:1.3.0")
    }
}
