package io.dflowers.user.controller

import arrow.core.raise.get
import arrow.core.raise.mapError
import io.dflowers.user.dto.ErrorCode
import io.dflowers.user.dto.SignInRequest
import io.dflowers.user.dto.SignInResponse
import io.dflowers.user.dto.SignUpRequest
import io.dflowers.user.dto.SignUpResponse
import io.dflowers.user.dto.UserResponse
import io.dflowers.user.entity.User
import io.dflowers.user.exception.HttpException
import io.dflowers.user.service.FindOneUser
import io.dflowers.user.service.SignUp
import io.dflowers.user.util.JwtUtil
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class AuthController(
    private val findOneUser: FindOneUser,
    private val signUp: SignUp,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    @PostMapping("/sign-in")
    suspend fun signIn(
        @Valid @RequestBody body: SignInRequest,
    ): ResponseEntity<SignInResponse> {
        val user =
            findOneUser(User.Email(body.email)).get()
                ?: throw HttpException.NotFound.create(objectName = User::class.java.name, objectId = body.email)

        if (!passwordEncoder.matches(body.password, user.password.value)) {
            throw HttpException.Unauthorized.create(ErrorCode.WRONG_CREDENTIALS, "사용자 이메일 혹은 비밀번호가 올바르지 않습니다.")
        }

        return ResponseEntity.ok().body(SignInResponse.from(jwtUtil.createAuthTokens(body.email)))
    }

    @PostMapping("/sign-up")
    suspend fun signUp(
        @Valid @RequestBody body: SignUpRequest,
    ): ResponseEntity<SignUpResponse> {
        val encoded = passwordEncoder.encode(body.password)
        val user =
            signUp(
                User.Email(body.email),
                User.Password(encoded),
                User.Name(body.name),
            ).mapError {
                when (it) {
                    SignUp.Failure.AlreadyExists -> throw HttpException.BadRequest.create(
                        code = ErrorCode.ALREADY_REGISTERED,
                        message = "이미 존재하는 사용자입니다.",
                    )
                }
            }.get()
        return ResponseEntity.status(HttpStatus.CREATED).body(SignUpResponse(user = UserResponse.from(user)))
    }
}
