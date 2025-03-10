package io.dflowers.user.controller

import arrow.core.raise.fold
import io.dflowers.user.entity.OAuth2Provider
import io.dflowers.user.service.SignInWithOAuth2
import io.dflowers.user.service.SignUpWithOAuth2
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/auth")
class AuthController(
    private val signInWithOAuth2: SignInWithOAuth2,
    private val signUpWithOAuth2: SignUpWithOAuth2,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/google/signin/verify-code")
    suspend fun handleGoogleCallback(
        @RequestParam("code") code: String,
        exchange: ServerWebExchange,
    ) {
        logger.info { "requested google code: $code" }

        signInWithOAuth2(code, OAuth2Provider.GOOGLE).fold(
            recover = {
                when (it) {
                    SignInWithOAuth2.Failure.UserNotFound -> {
                        exchange.response.setStatusCode(HttpStatus.FOUND)
                        exchange.response.headers.location = URI.create("/auth/failure")
                    }
                }
            },
            transform = {
                exchange.response.setStatusCode(HttpStatus.FOUND)
                exchange.response.headers.location =
                    UriComponentsBuilder
                        .fromPath("/auth/success")
                        .queryParam("access_token", it.accessToken)
                        .queryParam("access_token_expires_in", it.accessTokenExpiresIn)
                        .queryParam("refresh_token", it.refreshToken)
                        .queryParam("refresh_token_expires_in", it.refreshTokenExpiresIn)
                        .build()
                        .toUri()
            },
        )
    }

    @GetMapping("/google/signup/verify-code")
    suspend fun handleGoogleCallbackForSignUp(
        @RequestParam("code") code: String,
        exchange: ServerWebExchange,
    ) {
        logger.info { "requested google code for sign up: $code" }

        signUpWithOAuth2(code, OAuth2Provider.GOOGLE).fold(
            recover = {
                when (it) {
                    SignUpWithOAuth2.Failure.AlreadyExists -> {
                        exchange.response.setStatusCode(HttpStatus.FOUND)
                        exchange.response.headers.location = URI.create("/auth/failure")
                    }
                }
            },
            transform = {
                exchange.response.setStatusCode(HttpStatus.FOUND)
                exchange.response.headers.location =
                    UriComponentsBuilder
                        .fromPath("/auth/success")
                        .build()
                        .toUri()
            },
        )
    }

    /**
     * It is not required for production
     */
    @GetMapping("/success")
    fun successPage(): String = "Sign in successfully"

    @GetMapping("/failure")
    fun failurePage(): String = "Sign in failed"
}
