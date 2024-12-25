package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.core.raise.ensureNotNull
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FindOneUser(
    private val userRepository: UserRepository,
) {
    sealed interface Failure {
        data object NotFoundUser : Failure
    }

    operator fun invoke(email: User.Email): Effect<Failure, User> =
        effect {
            ensureNotNull(userRepository.findOneByEmail(email).bind()) {
                raise(Failure.NotFoundUser)
            }
        }
}
