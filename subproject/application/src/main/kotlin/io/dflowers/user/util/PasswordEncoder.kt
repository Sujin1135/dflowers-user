package io.dflowers.user.util

import arrow.core.raise.Effect

interface PasswordEncoder {
    fun encode(rawPassword: String): Effect<Nothing, String>

    fun matches(
        rawPassword: String,
        encodedPassword: String,
    ): Effect<Nothing, Boolean>
}
