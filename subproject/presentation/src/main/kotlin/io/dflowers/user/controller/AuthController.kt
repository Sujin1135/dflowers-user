package io.dflowers.user.controller

import arrow.core.raise.get
import arrow.core.raise.mapError
import io.dflowers.user.dto.ErrorCode
import io.dflowers.user.dto.SignUpRequest
import io.dflowers.user.dto.SignUpResponse
import io.dflowers.user.dto.UserResponse
import io.dflowers.user.entity.User
import io.dflowers.user.exception.HttpException
import io.dflowers.user.service.SignUp
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class AuthController(
    private val signUp: SignUp,
    private val passwordEncoder: PasswordEncoder,
) {
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
        return ResponseEntity.ok().body(SignUpResponse(user = UserResponse.from(user)))
    }
}
