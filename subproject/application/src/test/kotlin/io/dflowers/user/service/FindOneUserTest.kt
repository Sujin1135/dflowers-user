package io.dflowers.user.service

import io.dflowers.user.config.FlywayTestConfig
import io.dflowers.user.config.JooqTestConfig
import io.dflowers.user.config.TestcontainersConfig
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.flywaydb.core.Flyway
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.testcontainers.junit.jupiter.Testcontainers

@TestPropertySource(properties = ["spring.r2dbc.initialization-mode=always"])
@Testcontainers
@ContextConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    JooqTestConfig::class,
    FlywayTestConfig::class,
    TestcontainersConfig::class,
)
@SpringBootTest(classes = [FindOneUser::class, UserRepository::class, UserRepositoryImpl::class])
class FindOneUserTest(
    private val findOneUser: FindOneUser,
    private val flyway: Flyway,
) : FreeSpec({
        beforeEach {
            flyway.clean()
            flyway.migrate()
        }

        "add" - {
            1 + 3 shouldBe 4
        }
    })
