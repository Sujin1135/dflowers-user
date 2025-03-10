package io.dflowers.user.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OAuth2UserInfo(
    val email: String,
    val givenName: String,
    val familyName: String,
)
