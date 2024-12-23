package io.dflowers.user.dto

import io.dflowers.user.auth.TokenInfo

data class TokenInfoResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long,
) {
    companion object {
        fun from(tokenInfo: TokenInfo): TokenInfoResponse =
            TokenInfoResponse(
                accessToken = tokenInfo.accessToken,
                accessTokenExpiresIn = tokenInfo.accessTokenExpiresIn,
                refreshToken = tokenInfo.refreshToken,
                refreshTokenExpiresIn = tokenInfo.refreshTokenExpiresIn,
            )
    }
}
