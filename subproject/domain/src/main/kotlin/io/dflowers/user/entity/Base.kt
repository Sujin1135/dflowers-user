package io.dflowers.user.entity

import java.time.LocalDateTime
import java.util.UUID

interface Base {
    val id: UUID
    val created: Created
    val modified: Modified

    @JvmInline
    value class Created(
        val value: LocalDateTime,
    )

    @JvmInline
    value class Modified(
        val value: LocalDateTime,
    )
}
