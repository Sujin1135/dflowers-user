package io.dflowers.user.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class TestcontainersConfig {
    @Bean
    @ServiceConnection
    fun postgresqlContainer(): PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:17"))
}
