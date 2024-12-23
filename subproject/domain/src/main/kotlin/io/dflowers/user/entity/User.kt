package io.dflowers.user.entity

import java.time.LocalDateTime
import java.util.UUID

data class User(
    override val id: UUID,
    val email: String,
    val name: String,
    override val created: LocalDateTime,
    override val deleted: LocalDateTime?,
    override val modified: LocalDateTime,
) : SoftDeleteBase
