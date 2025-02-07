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
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
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
@SpringBootTest(classes = [UserRepositoryImpl::class])
class SignUpWithOauth2Test(
    private val flyway: Flyway,
    private val userRepository: UserRepository,
) : ShouldSpec({
        val oauth2CodeVerifier = mockk<OAuth2CodeVerifier>()
        val signUpWithOAuth2 = SignUpWithOAuth2(userRepository, oauth2CodeVerifier)
        val code = "test-code"
        val oauth2TokenResponseFixture = OAuth2TokenResponseGenerator.generate()
        val oauth2UserInfoFixture = OAuth2UserInfoGenerator.generate()

        beforeEach { flyway.migrate() }
        afterEach { flyway.clean() }

        context("sign up successfully done") {
            coEvery {
                oauth2CodeVerifier.verifyForSignUp(code)
            } returns effect { oauth2TokenResponseFixture }

            coEvery {
                oauth2CodeVerifier.findUserInfo(oauth2TokenResponseFixture.accessToken)
            } returns effect { oauth2UserInfoFixture }

            should("return new user that equals an information of the oauth2 user fixture ") {
                val user = signUpWithOAuth2(code, OAuth2Provider.GOOGLE).toEither().shouldBeRight()

                user.email.value shouldBe oauth2UserInfoFixture.email
                user.name.value shouldBe "${oauth2UserInfoFixture.familyName}${oauth2UserInfoFixture.givenName}"
            }
        }

        context("sign up failure cases") {
            coEvery {
                oauth2CodeVerifier.verifyForSignUp(code)
            } returns effect { oauth2TokenResponseFixture }

            coEvery {
                oauth2CodeVerifier.findUserInfo(oauth2TokenResponseFixture.accessToken)
            } returns effect { oauth2UserInfoFixture }

            should("raise the failure error cause user already exists") {
                val provider = OAuth2Provider.GOOGLE
                userRepository.insert(UserGenerator.generateByOAuth2(oauth2UserInfoFixture.email, provider)).get()

                signUpWithOAuth2(code, provider).toEither().shouldBeLeft().shouldBeTypeOf<SignUpWithOAuth2.Failure.AlreadyExists>()
            }
        }
    })
