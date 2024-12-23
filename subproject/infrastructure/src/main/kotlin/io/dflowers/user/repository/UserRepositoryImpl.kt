package io.dflowers.user.repository

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.dflowers.user.entity.Base
import io.dflowers.user.entity.SoftDeleteBase
import io.dflowers.user.entity.User
import io.dflowers.user.persistence.model.tables.Users.Companion.USERS
import io.dflowers.user.persistence.model.tables.records.UsersRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val dslContext: DSLContext,
) : UserRepository {
    override fun findOneByEmail(email: User.Email): Effect<Nothing, User?> =
        effect {
            dslContext
                .selectFrom(USERS)
                .where(USERS.EMAIL.eq(email.value))
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

    override fun insert(user: User): Effect<Nothing, User?> =
        effect {
            dslContext
                .insertInto(USERS)
                .set(user.toRecord())
                .awaitSingle()

            findOne(user.id.toString()).bind()
        }

    private fun UsersRecord.toDomain() =
        User(
            id = this.id!!,
            email = User.Email(this.email!!),
            password = User.Password(this.password!!),
            name = User.Name(this.name!!),
            deleted = SoftDeleteBase.Deleted(this.deleted),
            created = Base.Created(this.created!!),
            modified = Base.Modified(this.modified!!),
        )

    private fun User.toRecord() =
        UsersRecord(
            id = this.id,
            email = this.email.value,
            name = this.name.value,
            password = this.password.value,
            deleted = this.deleted?.value,
            created = this.created.value,
            modified = this.modified.value,
        )
}
