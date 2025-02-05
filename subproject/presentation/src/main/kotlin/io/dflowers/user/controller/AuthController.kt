package io.dflowers.user.controller

import arrow.core.raise.get
import io.dflowers.user.service.VerifyGoogleOAuth2Code
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import java.net.URI

@RestController
@RequestMapping("/auth")
class AuthController(
    private val verifyGoogleOAuth2Code: VerifyGoogleOAuth2Code,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/verify-code")
    suspend fun handleGoogleCallback(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
        exchange: ServerWebExchange,
    ) {
        logger.info { "requested google code: $code" }
        val tokenResponse = verifyGoogleOAuth2Code(code).get()
        logger.info { "response google token: $tokenResponse" }

        exchange.response.setStatusCode(HttpStatus.FOUND)
        exchange.response.headers.location = URI.create("/auth/success")
    }

    /**
     * It is not required for production
     */
    @GetMapping("/success")
    fun successPage(): String = "Sign in successfully"
}
