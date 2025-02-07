package io.dflowers.user.external

import arrow.core.raise.Effect
import io.dflowers.user.entity.OAuth2TokenResponse
import io.dflowers.user.entity.OAuth2UserInfo

// TODO: define left values
interface OAuth2CodeVerifier {
    fun verifyForSignIn(code: String): Effect<Nothing, OAuth2TokenResponse>

    fun verifyForSignUp(code: String): Effect<Nothing, OAuth2TokenResponse>

    fun findUserInfo(accessToken: String): Effect<Nothing, OAuth2UserInfo>
}
