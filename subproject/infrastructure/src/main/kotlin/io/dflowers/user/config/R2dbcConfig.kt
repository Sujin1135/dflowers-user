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

@Configuration
class R2dbcConfig {
    @Bean
    fun connectionFactory(): ConnectionFactory =
        ConnectionFactories.get(
            ConnectionFactoryOptions
                .builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 5432)
                .option(USER, "postgres")
                .option(PASSWORD, "password123!")
                .option(DATABASE, "user")
                .build(),
        )
}
