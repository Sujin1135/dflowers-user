package io.dflowers.user.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import io.dflowers.user.entity.OAuth2Provider
import io.dflowers.user.entity.User
import net.jqwik.api.Arbitraries

object UserGenerator {
    private val fixtureMonkey =
        FixtureMonkey
            .builder()
            .plugin(KotlinPlugin())
            .build()

    fun generateByOAuth2(
        email: String,
        provider: OAuth2Provider,
    ) = fixtureMonkey
        .giveMeBuilder<User>()
        .set("email", User.Email(email))
        .set("password", null)
        .set(
            "name",
            User.Name(
                Arbitraries
                    .strings()
                    .withCharRange('가', '힣')
                    .ofMinLength(2)
                    .ofMaxLength(4)
                    .sample(),
            ),
        ).set("provider", provider)
        .build()
        .sample()
}
