package io.dflowers.user.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import io.dflowers.user.entity.OAuth2UserInfo
import net.jqwik.api.Arbitraries

object OAuth2UserInfoGenerator {
    private val fixtureMonkey =
        FixtureMonkey
            .builder()
            .plugin(KotlinPlugin())
            .build()

    fun generate(): OAuth2UserInfo =
        fixtureMonkey
            .giveMeBuilder<OAuth2UserInfo>()
            .set(
                "email",
                "${Arbitraries
                    .strings()
                    .withCharRange('a', 'z')
                    .ofMinLength(5)
                    .ofMaxLength(16)
                    .sample()}@example.com",
            ).set(
                "givenName",
                Arbitraries
                    .strings()
                    .withCharRange('가', '힣')
                    .ofMinLength(1)
                    .ofMaxLength(3)
                    .sample(),
            ).set(
                "familyName",
                Arbitraries
                    .strings()
                    .withCharRange('가', '힣')
                    .ofMinLength(1)
                    .ofMaxLength(1)
                    .sample(),
            ).build()
            .sample()
}
