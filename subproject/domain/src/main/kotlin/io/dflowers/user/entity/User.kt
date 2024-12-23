package io.dflowers.user.entity

import java.time.LocalDateTime
import java.util.UUID

data class User(
    override val id: UUID,
    val email: Email,
    val password: Password,
    val name: Name,
    override val created: Base.Created,
    override val deleted: SoftDeleteBase.Deleted?,
    override val modified: Base.Modified,
) : SoftDeleteBase {
    companion object {
        fun of(
            email: Email,
            password: Password,
            name: Name,
        ): User {
            val now = LocalDateTime.now()
            return User(
                id = UUID.randomUUID(),
                email = email,
                password = password,
                name = name,
                deleted = null,
                created = Base.Created(now),
                modified = Base.Modified(now),
            )
        }
    }

    @JvmInline
    value class Email(
        val value: String,
    )

    @JvmInline
    value class Name(
        val value: String,
    )

    @JvmInline
    value class Password(
        val value: String,
    )
}
