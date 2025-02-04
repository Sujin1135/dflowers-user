package io.dflowers.user.service

import arrow.core.raise.toEither
import io.dflowers.user.config.FlywayTestConfig
import io.dflowers.user.config.JooqTestConfig
import io.dflowers.user.config.TestcontainersConfig
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.dflowers.user.util.Argon2IdPasswordEncoder
import io.dflowers.user.util.JwtUtil
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.types.shouldBeTypeOf
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
@SpringBootTest(
    classes = [
        SignIn::class,
        SignUp::class,
        FindOneUser::class,
        UserRepository::class,
        UserRepositoryImpl::class,
        Argon2IdPasswordEncoder::class,
        JwtUtil::class,
    ],
)
class SignInTest(
    private val signIn: SignIn,
    private val signUp: SignUp,
    private val flyway: Flyway,
) : FreeSpec({
        val email = User.Email("test@example.com")
        val password = User.Password("test123!")
        val name = User.Name("최민규")

        beforeTest {
            flyway.migrate()

            signUp(email, password, name).toEither()
        }

        afterTest {
            flyway.clean()
        }

        "should return the credential token" - {
            signIn(email, password).toEither().shouldBeRight()
        }

        "should raise failure cause not found user" - {
            signIn(User.Email("invalid@gmail.com"), password)
                .toEither()
                .shouldBeLeft()
                .shouldBeTypeOf<SignIn.Failure.NotFoundUser>()
        }
        "should raise failure cause wrong password" - {
            signIn(email, User.Password("invalid123!"))
                .toEither()
                .shouldBeLeft()
                .shouldBeTypeOf<SignIn.Failure.PasswordInvalid>()
        }
    })
