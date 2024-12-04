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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class R2dbcConfig(
    private val environment: Environment,
) {
    @Bean
    fun connectionFactory(): ConnectionFactory =
        ConnectionFactories.get(
            ConnectionFactoryOptions
                .builder()
                .option(DRIVER, "postgresql")
                .option(HOST, environment.getProperty("CONF_DB_HOST", "localhost"))
                .option(PORT, environment.getProperty("CONF_DB_PORT", "5432").toInt())
                .option(USER, environment.getProperty("CONF_DB_USERNAME", "postgres"))
                .option(PASSWORD, environment.getProperty("CONF_DB_PASSWORD", "password123!"))
                .option(DATABASE, environment.getProperty("CONF_DB_DATABASE", "user"))
                .build(),
        )
}
