package io.dflowers.user.service

import arrow.core.raise.effect
import arrow.core.raise.get
import arrow.core.raise.toEither
import io.dflowers.user.config.FlywayTestConfig
import io.dflowers.user.config.JooqTestConfig
import io.dflowers.user.config.TestcontainersConfig
import io.dflowers.user.entity.OAuth2Provider
import io.dflowers.user.external.OAuth2CodeVerifier
import io.dflowers.user.fixture.OAuth2TokenResponseGenerator
import io.dflowers.user.fixture.OAuth2UserInfoGenerator
import io.dflowers.user.fixture.UserGenerator
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.dflowers.user.util.JwtUtil
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.mockk
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
@SpringBootTest(classes = [UserRepositoryImpl::class, JwtUtil::class])
class SignInWithOauth2Test(
    private val flyway: Flyway,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
) : ShouldSpec({
        val oauth2CodeVerifier = mockk<OAuth2CodeVerifier>()
        val signInWithOAuth2 = SignInWithOAuth2(VerifyGoogleOAuth2Code(oauth2CodeVerifier), userRepository, jwtUtil)
        val tokenResponseFixture = OAuth2TokenResponseGenerator.generate()
        val oauth2UserInfoFixture = OAuth2UserInfoGenerator.generate()
        val provider = OAuth2Provider.GOOGLE
        val userFixture = UserGenerator.generateByOAuth2(oauth2UserInfoFixture.email, provider)
        val code = "test-code"

        beforeEach {
            flyway.migrate()
        }

        afterEach { flyway.clean() }

        coEvery {
            oauth2CodeVerifier.verifyForSignIn(code)
        } returns effect { tokenResponseFixture }

        coEvery {
            oauth2CodeVerifier.findUserInfo(tokenResponseFixture.accessToken)
        } returns effect { oauth2UserInfoFixture }

        context("sign in successfully done") {
            userRepository.insert(userFixture).get()

            should("return credential tokens") {
                signInWithOAuth2(code, provider).toEither().shouldBeRight()
            }
        }

        context("sign in failed cases") {
            should("raise the failure error cause user not found") {
                signInWithOAuth2(code, provider).toEither().shouldBeLeft().shouldBeTypeOf<SignInWithOAuth2.Failure.UserNotFound>()
            }
        }
    })
