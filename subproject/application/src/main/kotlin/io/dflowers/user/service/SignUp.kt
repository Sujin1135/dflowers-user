package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.core.raise.ensure
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.util.PasswordEncoder
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SignUp(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = KotlinLogging.logger {}

    sealed interface Failure {
        data object AlreadyExists : Failure
    }

    operator fun invoke(
        email: User.Email,
        password: User.Password,
        name: User.Name,
    ): Effect<Failure, User> =
        effect {
            logger.info { "started to sign up by email($email) and name($name)" }

            ensure(userRepository.findOneByEmail(email).bind() == null) {
                raise(Failure.AlreadyExists)
            }

            val hashed = passwordEncoder.encode(password.value).bind()
            userRepository.insert(User.defaultOf(email, User.Password(hashed), name)).bind()
        }
}
