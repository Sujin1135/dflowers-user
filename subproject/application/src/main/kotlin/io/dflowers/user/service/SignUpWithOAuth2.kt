package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.core.raise.ensure
import io.dflowers.user.entity.OAuth2Provider
import io.dflowers.user.entity.OAuth2UserInfo
import io.dflowers.user.entity.User
import io.dflowers.user.external.OAuth2CodeVerifier
import io.dflowers.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class SignUpWithOAuth2(
    private val userRepository: UserRepository,
    @Qualifier("google") private val googleOauth2CodeVerifier: OAuth2CodeVerifier,
) {
    sealed interface Failure {
        data object AlreadyExists : Failure
    }

    operator fun invoke(
        code: String,
        provider: OAuth2Provider,
    ): Effect<Failure, User> =
        effect {
            val userInfo = findUserInfo(provider, code).bind()
            val email = User.Email(userInfo.email)
            val name = User.Name("${userInfo.familyName}${userInfo.givenName}")

            ensure(userRepository.findOneByEmail(email).bind() == null) {
                raise(Failure.AlreadyExists)
            }
            userRepository.insert(User.providerOf(email, name, provider)).bind()
        }

    private fun findUserInfo(
        provider: OAuth2Provider,
        code: String,
    ): Effect<Nothing, OAuth2UserInfo> =
        effect {
            val oauth2CodeVerifier =
                when (provider) {
                    OAuth2Provider.GOOGLE -> googleOauth2CodeVerifier
                }
            val response = oauth2CodeVerifier.verifyForSignUp(code).bind()
            oauth2CodeVerifier.findUserInfo(response.accessToken).bind()
        }
}
