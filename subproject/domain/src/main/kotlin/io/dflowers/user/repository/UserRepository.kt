package io.dflowers.user.repository

import io.dflowers.user.entity.User

interface UserRepository {
    fun findUserByEmail(email: String): List<User>
}
