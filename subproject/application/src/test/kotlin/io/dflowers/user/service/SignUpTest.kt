package io.dflowers.user.service

import arrow.core.raise.getOrNull
import arrow.core.raise.toEither
import io.dflowers.user.config.FlywayTestConfig
import io.dflowers.user.config.JooqTestConfig
import io.dflowers.user.config.TestcontainersConfig
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.types.shouldBeTypeOf
import org.flywaydb.core.Flyway
import org.mindrot.jbcrypt.BCrypt
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
@SpringBootTest(classes = [SignUp::class, UserRepository::class, UserRepositoryImpl::class])
class SignUpTest(
    private val signUp: SignUp,
    private val flyway: Flyway,
) : FreeSpec({
        val email = User.Email("test@example.com")
        val password = User.Password(BCrypt.hashpw("test123!", BCrypt.gensalt()))
        val name = User.Name("최민규")

        beforeTest {
            flyway.clean()
            flyway.migrate()
        }

        "should return created user" - {
            signUp(email, password, name).toEither().shouldBeRight()
        }

        "should raise already exists failure because duplicated email existed" - {
            signUp(email, password, name).getOrNull()

            signUp(email, password, name).toEither().shouldBeLeft().shouldBeTypeOf<SignUp.Failure.AlreadyExists>()
        }
    })
