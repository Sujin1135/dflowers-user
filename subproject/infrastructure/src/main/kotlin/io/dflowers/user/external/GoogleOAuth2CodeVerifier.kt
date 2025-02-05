package io.dflowers.user.external

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.dflowers.user.entity.OAuth2TokenResponse
import io.dflowers.user.entity.OAuth2UserInfo
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component("google")
class GoogleOAuth2CodeVerifier(
    private val webClient: WebClient,
    @Value("\${oauth2.google.token_url}") private val googleTokenUrl: String,
    @Value("\${oauth2.google.redirect_uri}") private val googleRedirectUri: String,
    @Value("\${oauth2.google.client_id}") private val googleClientId: String,
    @Value("\${oauth2.google.secret_id}") private val googleSecretId: String,
) : OAuth2CodeVerifier {
    private val googleUserInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo"

    override fun verify(code: String): Effect<Nothing, OAuth2TokenResponse> =
        effect {
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
                .bodyToMono<OAuth2TokenResponse>()
                .awaitSingle()
        }

    override fun findUserInfo(accessToken: String): Effect<Nothing, OAuth2UserInfo> =
        effect {
            webClient
                .get()
                .uri(googleUserInfoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                .retrieve()
                .bodyToMono<OAuth2UserInfo>()
                .awaitSingle()
        }
}
