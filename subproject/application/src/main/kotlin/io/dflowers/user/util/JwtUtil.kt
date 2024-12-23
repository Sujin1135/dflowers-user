package io.dflowers.user.util

import io.dflowers.user.auth.AccessTokenClaims
import io.dflowers.user.auth.TokenInfo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.access-secret-key}") private val secret: String,
    @Value("\${jwt.refresh-secret-key}") private val refreshSecret: String,
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.access-token-expiration-millis}") private val accessTokenExpMillis: Long,
    @Value("\${jwt.refresh-token-expiration-millis}") private val refreshTokenExpMillis: Long,
) {
    private val accessKey by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)) }
    private val refreshKey by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret)) }

    fun createAuthTokens(username: String): TokenInfo {
        val now = Date()
        val accessTokenExpiresIn = Date(now.time + accessTokenExpMillis)
        val refreshTokenExpiresIn = Date(now.time + refreshTokenExpMillis)
        val accessToken = createToken(username, accessTokenExpiresIn, accessKey)
        val refreshToken = createToken(username, refreshTokenExpiresIn, refreshKey)

        return TokenInfo(
            accessToken = accessToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time,
            refreshToken = refreshToken,
            refreshTokenExpiresIn = refreshTokenExpiresIn.time,
        )
    }

    private fun createToken(
        username: String?,
        accessExpiration: Date,
        secretKey: SecretKey,
    ): String =
        Jwts
            .builder()
            .subject(username)
            .claim("role", "USER")
            .issuedAt(Date())
            .expiration(accessExpiration)
            .issuer(issuer)
            .signWith(secretKey)
            .compact()

    fun getAccessTokenClaims(token: String): AccessTokenClaims =
        Jwts
            .parser()
            .setSigningKey(refreshKey)
            .build()
            .parseClaimsJws(token)
            .body
            .toDomain()

    private fun Claims.toDomain() =
        AccessTokenClaims(
            username = subject,
            expiration = expiration,
            issuedAt = issuedAt,
            issuer = issuer,
        )
}
