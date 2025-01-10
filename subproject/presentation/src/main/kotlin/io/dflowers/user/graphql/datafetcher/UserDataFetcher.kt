package io.dflowers.user.graphql.datafetcher

import arrow.core.raise.get
import arrow.core.raise.mapError
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import io.dflowers.user.dto.ErrorCode
import io.dflowers.user.dto.SignUpRequest
import io.dflowers.user.entity.User
import io.dflowers.user.exception.HttpException
import io.dflowers.user.graphql.types.SignInInput
import io.dflowers.user.graphql.types.SignInResponse
import io.dflowers.user.graphql.types.SignUpResponse
import io.dflowers.user.graphql.types.TokenInfo
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
            val user =
                findOneUser(User.Email(input.email))
                    .mapError {
                        when (it) {
                            FindOneUser.Failure.NotFoundUser -> throw HttpException.NotFound.create(
                                objectName = User::class.simpleName!!,
                                objectId = input.email,
                            )
                        }
                    }.get()

            if (!passwordEncoder.matches(input.password, user.password.value)) {
                throw HttpException.Unauthorized.create(ErrorCode.WRONG_CREDENTIALS, "사용자 이메일 혹은 비밀번호가 올바르지 않습니다.")
            }
            val token = jwtUtil.createAuthTokens(input.email)
            SignInResponse(
                tokenInfo =
                    TokenInfo(
                        accessToken = token.accessToken,
                        accessTokenExpiresIn = token.accessTokenExpiresIn,
                        refreshToken = token.refreshToken,
                        refreshTokenExpiresIn = token.refreshTokenExpiresIn,
                    ),
            )
        }

    @DgsMutation
    fun signUp(
        @InputArgument input: SignUpRequest,
    ): Mono<SignUpResponse> =
        mono {
            val user =
                signUpUser(User.Email(input.email), User.Password(input.password), User.Name(input.name))
                    .mapError {
                        when (it) {
                            SignUp.Failure.AlreadyExists -> throw HttpException.BadRequest.create(
                                code = ErrorCode.ALREADY_REGISTERED,
                                message = "이미 존재하는 사용자입니다.",
                            )
                        }
                    }.get()
            SignUpResponse(
                user =
                    GraphQLUser(
                        id = user.id.toString(),
                        email = user.email.value,
                        name = user.name.value,
                        created = user.created.value,
                        modified = user.modified.value,
                    ),
            )
        }
}
