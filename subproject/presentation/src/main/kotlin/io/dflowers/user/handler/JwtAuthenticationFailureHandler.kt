package io.dflowers.user.handler

import com.fasterxml.jackson.databind.ObjectMapper
import io.dflowers.user.dto.ErrorCode
import io.dflowers.user.dto.ErrorResponse
import io.dflowers.user.exception.HttpException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFailureHandler(
    private val objectMapper: ObjectMapper,
) : ServerAuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange,
        exception: AuthenticationException,
    ): Mono<Void> {
        val response = webFilterExchange.exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        response.headers.contentType = MediaType.APPLICATION_JSON

        val errorResponse =
            ErrorResponse.of(
                HttpException.Unauthorized.create(
                    message = exception.message ?: "Unauthorized",
                    code = ErrorCode.INVALID_TOKEN,
                ),
            )
        val buffer =
            response.bufferFactory().wrap(
                objectMapper.writeValueAsBytes(errorResponse),
            )
        return response.writeWith(Mono.just(buffer))
    }
}
