package io.dflowers.user.service

import arrow.core.raise.Effect
import arrow.core.raise.effect
import arrow.core.raise.ensure
import arrow.core.raise.mapError
import io.dflowers.user.auth.TokenInfo
import io.dflowers.user.entity.User
import io.dflowers.user.util.JwtUtil
import io.dflowers.user.util.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class SignIn(
    private val findOneUser: FindOneUser,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    sealed interface Failure {
        data object NotFoundUser : Failure

        data object PasswordInvalid : Failure
    }

    operator fun invoke(
        email: User.Email,
        password: User.Password,
    ): Effect<Failure, TokenInfo> =
        effect {
            val user =
                findOneUser(email)
                    .mapError {
                        when (it) {
                            FindOneUser.Failure.NotFoundUser ->
                                raise(Failure.NotFoundUser)
                        }
                    }.bind()

            ensure(passwordEncoder.matches(password.value, user.password!!.value).bind()) {
                raise(Failure.PasswordInvalid)
            }
            jwtUtil.createAuthTokens(email.value)
        }
}
