package io.dflowers.user.entity

enum class OAuth2Provider(
    val value: String,
) {
    GOOGLE("google"),
    ;

    companion object {
        private val map = entries.associateBy(OAuth2Provider::value)

        fun fromValue(value: String): OAuth2Provider = map[value] ?: throw IllegalArgumentException("Unknown OAuth2Provider")
    }
}
