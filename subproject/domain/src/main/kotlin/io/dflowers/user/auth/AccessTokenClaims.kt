package io.dflowers.user.auth

import java.util.Date

data class AccessTokenClaims(
    val username: String,
    val expiration: Date,
    val issuedAt: Date,
    val issuer: String?,
)
