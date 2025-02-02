package io.dflowers.user.util

import arrow.core.raise.Effect
import arrow.core.raise.effect
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component

@Component
class Argon2IdPasswordEncoder(
    @Value("\${argon2.parameters.iterations}") private val iterations: Int,
    @Value("\${argon2.parameters.parallelism}") private val parallelism: Int,
    @Value("\${argon2.parameters.memory}") private val memory: Int,
) : PasswordEncoder {
    val argon2Encoder = Argon2PasswordEncoder(16, 32, parallelism, memory, iterations)

    override fun encode(rawPassword: String): Effect<Nothing, String> = effect { argon2Encoder.encode(rawPassword) }

    override fun matches(
        rawPassword: String,
        encodedPassword: String,
    ): Effect<Nothing, Boolean> = effect { argon2Encoder.matches(rawPassword, encodedPassword) }
}
