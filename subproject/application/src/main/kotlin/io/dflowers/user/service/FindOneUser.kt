package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.dflowers.user.entity.User
import io.dflowers.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FindOneUser(
    private val userRepository: UserRepository,
) {
    operator fun invoke(email: User.Email): Effect<Nothing, User?> =
        effect {
            userRepository.findOneByEmail(email).bind()
        }
}
