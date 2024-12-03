plugins {
    alias(libs.plugins.flyway)
    alias(libs.plugins.jooq)
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.postgresql)
        classpath(libs.flyway.postgresql)
        classpath(libs.jooq.codegen)
    }
}

dependencies {
    implementation(project(":subproject:domain"))

    implementation(libs.bundles.db)

    implementation(libs.bundles.flyway)
}

val dbHost: String = System.getenv("DB_HOST") ?: "127.0.0.1"
val dbPort: String = System.getenv("DB_PORT") ?: "5432"
val dbSchema: String = System.getenv("DB_SCHEMA") ?: "user"
val dbUser: String = System.getenv("DB_USER") ?: "postgres"
val dbPassword: String = System.getenv("DB_PASSWORD") ?: "password123!"

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://$dbHost:$dbPort/:$dbSchema"
    baselineVersion = "1"
    baselineDescription = "true"
    table = "migration_history"
    user = dbUser
    password = dbPassword
    connectRetries = 60
    locations =
        listOf("filesystem:src/main/resources/db/migration/")
            .toTypedArray()
    cleanDisabled = false
}
