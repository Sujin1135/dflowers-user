package io.dflowers.user.repository

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.dflowers.user.entity.User
import io.dflowers.user.persistence.model.tables.Users.Companion.USERS
import io.dflowers.user.persistence.model.tables.records.UsersRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val dslContext: DSLContext,
) : UserRepository {
    override fun findOneByEmail(email: String): Effect<Nothing, User?> =
        effect {
            dslContext
                .selectFrom(USERS)
                .where(USERS.EMAIL.eq(email))
                .awaitFirstOrNull()
                ?.toDomain()
        }

    override fun findOne(id: String): Effect<Nothing, User?> =
        effect {
            dslContext
                .selectFrom(USERS)
                .where(USERS.ID.eq(UUID.fromString(id)))
                .awaitFirstOrNull()
                ?.toDomain()
        }

    private fun UsersRecord.toDomain() =
        User(
            id = this.id.toString(),
            email = this.email!!,
        )
}
