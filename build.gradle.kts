import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.detekt)
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
    apply(
        plugin =
            rootProject.libs.plugins.detekt
                .get()
                .pluginId,
    )

    dependencies {
        implementation(rootProject.libs.spring.boot.webflux)
        implementation(rootProject.libs.jackson.module.kotlin)
        implementation(rootProject.libs.reactor.kotlin.extensions)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.coroutines)
        implementation(rootProject.libs.bundles.arrow.kt)

        detektPlugins(rootProject.libs.detekt.rules)

        implementation(rootProject.libs.block.hound)

        compileOnly(rootProject.libs.tomcat)

        testImplementation(rootProject.libs.spring.boot.test)
        testImplementation(rootProject.libs.reactor.test)
        testImplementation(rootProject.libs.junit5)
        testImplementation(rootProject.libs.bundles.kotest)
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
