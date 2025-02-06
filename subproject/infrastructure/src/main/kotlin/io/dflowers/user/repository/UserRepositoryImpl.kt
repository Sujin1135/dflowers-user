package io.dflowers.user.repository

import arrow.core.raise.Effect
import arrow.core.raise.effect
import io.dflowers.user.entity.Base
import io.dflowers.user.entity.OAuth2Provider
import io.dflowers.user.entity.SoftDeleteBase
import io.dflowers.user.entity.User
import io.dflowers.user.persistence.model.tables.Users.Companion.USERS
import io.dflowers.user.persistence.model.tables.records.UsersRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.ZoneOffset
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

    override fun insert(user: User): Effect<Nothing, User> =
        effect {
            dslContext
                .insertInto(USERS)
                .set(user.toRecord())
                .awaitSingle()

            findOne(user.id.toString()).bind()!!
        }

    private fun UsersRecord.toDomain() =
        User(
            id = this.id!!,
            email = User.Email(this.email!!),
            password = this.password?.let { User.Password(it) },
            name = User.Name(this.name!!),
            provider = this.provider?.let { OAuth2Provider.fromValue(it) },
            deleted = SoftDeleteBase.Deleted(this.deleted?.atOffset(ZoneOffset.UTC)),
            created = Base.Created(this.created!!.atOffset(ZoneOffset.UTC)),
            modified = Base.Modified(this.modified!!.atOffset(ZoneOffset.UTC)),
        )

    private fun User.toRecord() =
        UsersRecord(
            id = this.id,
            email = this.email.value,
            name = this.name.value,
            password = this.password?.value,
            provider = this.provider?.value,
            deleted = this.deleted?.value?.toLocalDateTime(),
            created = this.created.value.toLocalDateTime(),
            modified = this.modified.value.toLocalDateTime(),
        )
}
