package io.dflowers.user.util

import io.dflowers.user.dto.BearerToken
import io.dflowers.user.dto.Principal
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    private val jwtUtil: JwtUtil,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        Mono
            .justOrEmpty(authentication)
            .cast(BearerToken::class.java)
            .handle { it, sink ->
                try {
                    val claims = jwtUtil.getAccessTokenClaims(it.credentials)

                    sink.next(this.getAuthentication(claims.username))
                } catch (e: RuntimeException) {
                    sink.error(AuthenticationServiceException("Invalid Token", e))
                }
            }

    private fun getAuthentication(username: String): Authentication =
        UsernamePasswordAuthenticationToken(
            Principal(username, listOf()),
            null,
            listOf(),
        )
}
