package io.dflowers.user.dto

import jakarta.validation.constraints.Email

data class SignInRequest(
    @Email
    val email: String,
    val password: String,
)
