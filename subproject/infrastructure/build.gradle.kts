import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

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

    api(libs.bundles.db)
    api(libs.bundles.flyway)

    testApi(libs.bundles.testcontainers)

    jooqGenerator(libs.postgresql)
}

val dbHost: String = System.getenv("DB_HOST") ?: "127.0.0.1"
val dbPort: String = System.getenv("DB_PORT") ?: "5432"
val dbSchema: String = System.getenv("DB_SCHEMA") ?: "user"
val dbUser: String = System.getenv("DB_USER") ?: "postgres"
val dbPassword: String = System.getenv("DB_PASSWORD") ?: "password123!"

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://$dbHost:$dbPort/$dbSchema"
    baselineVersion = "1"
    baselineDescription = "true"
    user = dbUser
    password = dbPassword
    connectRetries = 60
    locations =
        listOf("filesystem:src/main/resources/db/migration/")
            .toTypedArray()
    cleanDisabled = false
}

jooq {
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://$dbHost:$dbPort/$dbSchema"
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        forcedTypes.addAll(
                            listOf(
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "JSONB?"
                                },
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "INET"
                                },
                            ),
                        )
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "nu.studer.sample"
                        directory = "build/generated-src/jooq/main"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
