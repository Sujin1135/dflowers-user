package io.dflowers.user.config

import org.flywaydb.core.Flyway
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration
class FlywayTestConfig {
    @Bean
    fun flyway(postgresqlContainer: PostgreSQLContainer<*>): Flyway =
        Flyway
            .configure()
            .dataSource(
                postgresqlContainer.jdbcUrl,
                postgresqlContainer.username,
                postgresqlContainer.password,
            ).locations("classpath:db/migration")
            .cleanDisabled(false)
            .load()
}
