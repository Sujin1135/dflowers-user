package io.dflowers.user.entity

import java.time.LocalDateTime
import java.util.UUID

interface Base {
    val id: UUID
    val created: LocalDateTime
    val modified: LocalDateTime
}
