package io.dflowers.user.service

import arrow.core.raise.toEither
import io.dflowers.user.config.FlywayTestConfig
import io.dflowers.user.config.JooqTestConfig
import io.dflowers.user.config.TestcontainersConfig
import io.dflowers.user.persistence.model.tables.Users.Companion.USERS
import io.dflowers.user.persistence.model.tables.records.UsersRecord
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactive.awaitLast
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime
import java.util.UUID

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
    private val dslContext: DSLContext,
) : FreeSpec({
        val testId = UUID.randomUUID()
        val email = "test@test.com"

        beforeTest {
            flyway.clean()
            flyway.migrate()
            dslContext
                .insertInto(USERS)
                .set(
                    UsersRecord(
                        id = testId,
                        email = email,
                        name = "test",
                        password = UUID.randomUUID().toString(),
                        deleted = null,
                        created = LocalDateTime.now(),
                        modified = LocalDateTime.now(),
                    ),
                ).awaitLast()
        }

        "should returns an existing user by the user email" - {
            val sut = findOneUser(email).toEither().shouldBeRight()

            sut shouldNotBe null
            sut?.id shouldBe testId
            sut?.email shouldBe email
        }
    })
