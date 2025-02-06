package io.dflowers.user.entity

import java.time.OffsetDateTime
import java.util.UUID

data class User(
    override val id: UUID,
    val email: Email,
    val password: Password? = null,
    val name: Name,
    val provider: OAuth2Provider? = null,
    override val created: Base.Created,
    override val deleted: SoftDeleteBase.Deleted?,
    override val modified: Base.Modified,
) : SoftDeleteBase {
    companion object {
        fun defaultOf(
            email: Email,
            password: Password,
            name: Name,
        ): User {
            val now = OffsetDateTime.now()
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

        fun providerOf(
            email: Email,
            name: Name,
            provider: OAuth2Provider,
        ): User {
            val now = OffsetDateTime.now()
            return User(
                id = UUID.randomUUID(),
                email = email,
                password = null,
                name = name,
                deleted = null,
                provider = provider,
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
