package io.dflowers.user.dto

import io.dflowers.user.auth.TokenInfo

data class SignInResponse(
    val tokenInfo: TokenInfoResponse,
) {
    companion object {
        fun from(tokenInfo: TokenInfo): SignInResponse =
            SignInResponse(
                tokenInfo = TokenInfoResponse.from(tokenInfo),
            )
    }
}
