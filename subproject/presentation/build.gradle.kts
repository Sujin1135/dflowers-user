plugins {
    alias(libs.plugins.dgs.codegen)
}

dependencies {
    implementation(project(":subproject:interface"))
    implementation(project(":subproject:application"))
    implementation(project(":subproject:domain"))

    implementation(libs.grpc.spring)
    implementation(libs.grpc.services)
    implementation(libs.spring.security)
    implementation(libs.swagger)
    implementation(libs.spring.validation)
    implementation(libs.bundles.graphql)
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    schemaPaths = listOf("$projectDir/src/main/resources/schema").toMutableList() // 스키마 경로
    packageName = "io.dflowers.user.graphql" // 생성될 Java/Kotlin 클래스 패키지 경로
    generateClient = true // 타입 안전한 쿼리 API 생성 활성화
}
