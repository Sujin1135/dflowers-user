package io.dflowers.user.repository

import io.dflowers.user.entity.User
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val dslContext: DSLContext,
) : UserRepository {
    override fun findUserByEmail(email: String): List<User> = listOf(User("user-1135", "sattlub123@gmail.com"))
}
