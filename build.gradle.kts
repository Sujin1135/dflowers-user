plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
}

group = "io.dflowers"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(":subproject:interface"))
    implementation(project(":subproject:presentation"))
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:application"))
    implementation(project(":subproject:infrastructure"))
}

allprojects {
    apply(
        plugin =
            rootProject.libs.plugins.kotlin.jvm
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.kotlin.spring
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.spring.boot
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.dependency.management
                .get()
                .pluginId,
    )

    dependencies {
        implementation(rootProject.libs.spring.boot.webflux)
        implementation(rootProject.libs.jackson.module.kotlin)
        implementation(rootProject.libs.reactor.kotlin.extensions)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.coroutines)

        compileOnly(rootProject.libs.tomcat)

        testImplementation(rootProject.libs.spring.boot.test)
        testImplementation(rootProject.libs.reactor.test)
        testImplementation(rootProject.libs.junit5)
        testRuntimeOnly(rootProject.libs.junit.platform.launcher)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
