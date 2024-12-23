package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.core.raise.ensure
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository

class SignUp(
    private val userRepository: UserRepository,
) {
    sealed interface Failure {
        data object AlreadyExists : Failure
    }

    operator fun invoke(
        email: User.Email,
        password: User.Password,
        name: User.Name,
    ): Effect<Failure, User?> =
        effect {
            ensure(userRepository.findOneByEmail(email).bind() == null) {
                raise(Failure.AlreadyExists)
            }

            userRepository.insert(User.of(email, password, name)).bind()
        }
}
