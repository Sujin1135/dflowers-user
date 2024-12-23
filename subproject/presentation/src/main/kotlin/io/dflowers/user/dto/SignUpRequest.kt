package io.dflowers.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignUpRequest(
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @Max(value = 254, message = "이메일은 254자 내외로 입력해야 합니다.")
    @NotBlank
    val email: String,
    @NotBlank
    @Pattern(
        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8.20}",
        message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8 ~20자의 비밀번호여야 합니다.",
    )
    val password: String,
    @NotBlank
    @Max(50, message = "이름은 50자 내외로 입력해야 합니다.")
    val name: String,
)
