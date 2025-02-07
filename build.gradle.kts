import io.gitlab.arturbosch.detekt.Detekt
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.detekt)
    `java-test-fixtures`
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

subprojects {
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
    apply(
        plugin =
            rootProject.libs.plugins.detekt
                .get()
                .pluginId,
    )
    apply(plugin = "java-test-fixtures")

    dependencies {
        implementation(rootProject.libs.spring.boot.webflux)
        implementation(rootProject.libs.jackson.module.kotlin)
        implementation(rootProject.libs.reactor.kotlin.extensions)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.coroutines)
        implementation(rootProject.libs.bundles.arrow.kt)
        implementation(rootProject.libs.bundles.logging)
        implementation(rootProject.libs.block.hound)
        implementation(rootProject.libs.bundles.tracing)

        detektPlugins(rootProject.libs.detekt.rules)

        compileOnly(rootProject.libs.tomcat)

        testImplementation(rootProject.libs.spring.boot.test)
        testImplementation(rootProject.libs.reactor.test)
        testImplementation(rootProject.libs.junit5)
        testImplementation(rootProject.libs.bundles.kotest)
        testImplementation(rootProject.libs.mockk)
        testFixturesImplementation(rootProject.libs.fixture.monkey)
        testRuntimeOnly(rootProject.libs.junit.platform.launcher)
    }

    afterEvaluate {
        detekt {
            toolVersion =
                rootProject.libs.plugins.detekt
                    .get()
                    .version
                    .toString()
            buildUponDefaultConfig = false
            config.setFrom(files("$rootDir/detekt-config.yml"))
        }
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
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

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
