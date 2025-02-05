package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.dflowers.user.entity.OAuth2UserInfo
import io.dflowers.user.external.OAuth2CodeVerifier
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class VerifyGoogleOAuth2Code(
    @Qualifier("google") private val oauth2CodeVerifier: OAuth2CodeVerifier,
) {
    operator fun invoke(code: String): Effect<Nothing, OAuth2UserInfo> =
        effect {
            val tokenResponse = oauth2CodeVerifier.verify(code).bind()
            oauth2CodeVerifier.findUserInfo(tokenResponse.accessToken).bind()
        }
}
