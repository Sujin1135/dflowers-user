plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
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
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.5")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.3")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.25")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")

        compileOnly("org.apache.tomcat:annotations-api:6.0.53")

        testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.5")
        testImplementation("io.projectreactor:reactor-test:3.7.0")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.1.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
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
