package io.dflowers.user.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration
class JooqTestConfig {
    @Bean
    fun connectionFactory(postgresqlContainer: PostgreSQLContainer<*>): ConnectionFactory {
        val options =
            ConnectionFactoryOptions
                .builder()
                .option(DRIVER, "postgres")
                .option(HOST, postgresqlContainer.host)
                .option(PORT, postgresqlContainer.firstMappedPort)
                .option(USER, postgresqlContainer.username)
                .option(PASSWORD, postgresqlContainer.password)
                .option(DATABASE, postgresqlContainer.databaseName)
                .build()

        return ConnectionFactories.get(options)
    }

    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext = DSL.using(connectionFactory, SQLDialect.POSTGRES)
}
