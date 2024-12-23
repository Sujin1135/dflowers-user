package io.dflowers.user.entity

import java.time.LocalDateTime

interface SoftDeleteBase : Base {
    val deleted: Deleted?

    @JvmInline
    value class Deleted(
        val value: LocalDateTime?,
    )
}
