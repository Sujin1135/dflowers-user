package io.dflowers.user.repository

import arrow.core.raise.Effect
import io.dflowers.user.entity.User

interface UserRepository {
    fun findOneByEmail(email: User.Email): Effect<Nothing, User?>

    fun findOne(id: String): Effect<Nothing, User?>

    fun insert(user: User): Effect<Nothing, User?>
}
