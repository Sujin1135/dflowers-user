package io.dflowers.user.service

import arrow.core.raise.get
import arrow.core.raise.getOrNull
import arrow.core.raise.toEither
import io.dflowers.user.config.FlywayTestConfig
import io.dflowers.user.config.JooqTestConfig
import io.dflowers.user.config.TestcontainersConfig
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.dflowers.user.util.Argon2IdPasswordEncoder
import io.dflowers.user.util.PasswordEncoder
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
@SpringBootTest(classes = [SignUp::class, UserRepository::class, UserRepositoryImpl::class, Argon2IdPasswordEncoder::class])
class SignUpTest(
    private val signUp: SignUp,
    private val flyway: Flyway,
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : FreeSpec({
        val email = User.Email("test@example.com")
        val password = User.Password("test123!")
        val name = User.Name("최민규")

        beforeTest {
            flyway.migrate()
        }

        afterTest {
            flyway.clean()
        }

        "should return created user" - {
            signUp(email, password, name).toEither().shouldBeRight()
        }

        "should return the correctly hashed password" - {
            signUp(email, password, name).toEither().shouldBeRight()

            val found = repository.findOneByEmail(email).get()

            found!!.password shouldNotBe password
            passwordEncoder.matches(password.value, found.password!!.value).get().shouldBe(true)
        }

        "should raise already exists failure because duplicated email existed" - {
            signUp(email, password, name).getOrNull()

            signUp(email, password, name).toEither().shouldBeLeft().shouldBeTypeOf<SignUp.Failure.AlreadyExists>()
        }
    })
