package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.core.raise.ensureNotNull
import io.dflowers.user.auth.TokenInfo
import io.dflowers.user.entity.OAuth2Provider
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.util.JwtUtil
import org.springframework.stereotype.Service

@Service
class SignInWithOAuth2(
    private val verifyGoogleOAuth2Code: VerifyGoogleOAuth2Code,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
) {
    sealed interface Failure {
        data object UserNotFound : Failure
    }

    operator fun invoke(
        code: String,
        provider: OAuth2Provider,
    ): Effect<Failure, TokenInfo> =
        effect {
            val oauth2UserInfo =
                when (provider) {
                    OAuth2Provider.GOOGLE -> {
                        verifyGoogleOAuth2Code(code).bind()
                    }
                }

            val user =
                ensureNotNull(userRepository.findOneByEmail(User.Email(oauth2UserInfo.email)).bind()) {
                    raise(Failure.UserNotFound)
                }

            jwtUtil.createAuthTokens(user.email.value)
        }
}
