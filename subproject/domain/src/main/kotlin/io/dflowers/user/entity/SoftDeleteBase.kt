package io.dflowers.user.entity

import java.time.OffsetDateTime

interface SoftDeleteBase : Base {
    val deleted: Deleted?

    @JvmInline
    value class Deleted(
        val value: OffsetDateTime?,
    )
}
