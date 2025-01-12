package io.dflowers.user.graphql.datafetcher

import arrow.core.raise.fold
import arrow.core.raise.getOrElse
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import io.dflowers.user.dto.SignUpRequest
import io.dflowers.user.entity.User
import io.dflowers.user.graphql.types.AlreadyExistedError
import io.dflowers.user.graphql.types.SignInInput
import io.dflowers.user.graphql.types.SignInPayload
import io.dflowers.user.graphql.types.SignInResponse
import io.dflowers.user.graphql.types.SignUpPayload
import io.dflowers.user.graphql.types.SignUpResponse
import io.dflowers.user.graphql.types.TokenInfo
import io.dflowers.user.graphql.types.UnauthorizedError
import io.dflowers.user.service.FindOneUser
import io.dflowers.user.service.SignUp
import io.dflowers.user.util.JwtUtil
import kotlinx.coroutines.reactor.mono
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import io.dflowers.user.graphql.types.User as GraphQLUser

@DgsComponent
class UserDataFetcher(
    private val signUpUser: SignUp,
    private val findOneUser: FindOneUser,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    @DgsMutation
    fun signIn(
        @InputArgument input: SignInInput,
    ): Mono<SignInResponse> =
        mono {
            val unauthorizedErrorMessage = "사용자 이메일 혹은 비밀번호가 올바르지 않습니다."
            val user =
                findOneUser(User.Email(input.email))
                    .getOrElse {
                        return@mono when (it) {
                            FindOneUser.Failure.NotFoundUser ->
                                UnauthorizedError(
                                    message = unauthorizedErrorMessage,
                                )
                        }
                    }

            if (!passwordEncoder.matches(input.password, user.password.value)) {
                UnauthorizedError(message = unauthorizedErrorMessage)
            } else {
                val token = jwtUtil.createAuthTokens(input.email)
                SignInPayload(
                    tokenInfo =
                        TokenInfo(
                            accessToken = token.accessToken,
                            accessTokenExpiresIn = token.accessTokenExpiresIn,
                            refreshToken = token.refreshToken,
                            refreshTokenExpiresIn = token.refreshTokenExpiresIn,
                        ),
                )
            }
        }

    @DgsMutation
    fun signUp(
        @InputArgument input: SignUpRequest,
    ): Mono<SignUpResponse> =
        mono {
            signUpUser(
                User.Email(input.email),
                User.Password(input.password),
                User.Name(input.name),
            ).fold(
                recover = {
                    when (it) {
                        SignUp.Failure.AlreadyExists -> AlreadyExistedError(message = "이미 존재하는 사용자입니다.")
                    }
                },
                transform = {
                    SignUpPayload(
                        GraphQLUser(
                            id = it.id.toString(),
                            email = it.email.value,
                            name = it.name.value,
                            created = it.created.value,
                            modified = it.modified.value,
                        ),
                    )
                },
            )
        }
}
