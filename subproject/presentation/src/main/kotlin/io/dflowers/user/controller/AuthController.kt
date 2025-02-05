package io.dflowers.user.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.server.ServerWebExchange
import java.net.URI

@RestController
@RequestMapping("/auth")
class AuthController(
    private val webClient: WebClient,
    @Value("\${oauth2.google.token_url}") private val googleTokenUrl: String,
    @Value("\${oauth2.google.redirect_uri}") private val googleRedirectUri: String,
    @Value("\${oauth2.google.client_id}") private val googleClientId: String,
    @Value("\${oauth2.google.secret_id}") private val googleSecretId: String,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/verify-code")
    suspend fun handleGoogleCallback(
        @RequestParam("code") code: String,
        exchange: ServerWebExchange,
    ) {
        logger.info { "requested google code: $code" }
        val tokenResponse = exchangeCodeForToken(code)

        // TODO: issue user found check and a credential token

        exchange.response.setStatusCode(HttpStatus.FOUND)
        exchange.response.headers.location = URI.create("/success")
    }

    @GetMapping("/success")
    fun successPage(): String = "Sign in successfully"

    // TODO: should move each application / infrastructure layers
    private suspend fun exchangeCodeForToken(code: String): Map<String, Any> =
        webClient
            .post()
            .uri(googleTokenUrl)
            .bodyValue(
                mapOf(
                    "code" to code,
                    "client_id" to googleClientId,
                    "client_secret" to googleSecretId,
                    "redirect_uri" to googleRedirectUri,
                    "grant_type" to "authorization_code",
                ),
            ).retrieve()
            .bodyToMono<Map<String, Any>>()
            .awaitSingle()
}
