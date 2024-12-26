package io.dflowers.user.entity

import java.time.OffsetDateTime
import java.util.UUID

interface Base {
    val id: UUID
    val created: Created
    val modified: Modified

    @JvmInline
    value class Created(
        val value: OffsetDateTime,
    )

    @JvmInline
    value class Modified(
        val value: OffsetDateTime,
    )
}
