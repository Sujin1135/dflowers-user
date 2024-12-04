package io.dflowers.user.repository

import io.dflowers.user.entity.User

interface UserRepository {
    suspend fun findUserByEmail(email: String): List<User>
}
