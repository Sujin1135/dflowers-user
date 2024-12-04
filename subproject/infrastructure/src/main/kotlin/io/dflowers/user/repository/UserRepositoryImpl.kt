package io.dflowers.user.repository

import io.dflowers.user.entity.User
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import nu.studer.sample.tables.Users.USERS
import nu.studer.sample.tables.records.UsersRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val dslContext: DSLContext,
) : UserRepository {
    override suspend fun findUserByEmail(email: String): List<User> =
        dslContext
            .selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .asFlow()
            .toList()
            .map { it.toDomain() }

    private fun UsersRecord.toDomain() =
        User(
            id = this.id.toString(),
            email = this.email,
        )
}
