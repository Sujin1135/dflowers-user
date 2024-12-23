package io.dflowers.user.dto

import io.dflowers.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val name: String,
    val created: LocalDateTime,
    val modified: LocalDateTime,
) {
    companion object {
        fun from(user: User): UserResponse =
            UserResponse(
                id = user.id,
                email = user.email.value,
                name = user.name.value,
                created = user.created.value,
                modified = user.modified.value,
            )
    }
}
