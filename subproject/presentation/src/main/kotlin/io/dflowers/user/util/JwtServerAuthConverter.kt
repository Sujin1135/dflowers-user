package io.dflowers.user.util

import io.dflowers.user.dto.BearerToken
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtServerAuthConverter : ServerAuthenticationConverter {
    companion object {
        const val GRANT_TYPE = "Bearer "
    }

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> =
        Mono
            .justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { header -> header.startsWith(GRANT_TYPE) }
            .map { header -> header.substring(GRANT_TYPE.length) }
            .map { token -> BearerToken(token) }
}
