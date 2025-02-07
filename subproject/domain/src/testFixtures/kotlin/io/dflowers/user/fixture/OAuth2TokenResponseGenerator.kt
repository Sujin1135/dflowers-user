package io.dflowers.user.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import io.dflowers.user.entity.OAuth2TokenResponse
import net.jqwik.api.Arbitraries

object OAuth2TokenResponseGenerator {
    private val fixtureMonkey =
        FixtureMonkey
            .builder()
            .plugin(KotlinPlugin())
            .build()

    fun generate() =
        fixtureMonkey
            .giveMeBuilder<OAuth2TokenResponse>()
            .set(
                "accessToken",
                Arbitraries
                    .strings()
                    .withCharRange('a', 'z')
                    .ofMinLength(16)
                    .ofMaxLength(16)
                    .sample(),
            ).build()
            .sample()
}
