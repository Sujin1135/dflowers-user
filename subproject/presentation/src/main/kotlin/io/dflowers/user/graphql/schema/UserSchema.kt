package io.dflowers.user.graphql.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.dflowers.user.entity.User
import java.time.LocalDateTime

@GraphQLDescription("App user")
data class UserSchema(
    val id: String,
    val email: String,
    val name: String,
    val created: LocalDateTime,
    val modified: LocalDateTime,
) {
    companion object {
        fun from(user: User): UserSchema =
            UserSchema(
                id = user.id.toString(),
                email = user.email.value,
                name = user.name.value,
                created = user.created.value,
                modified = user.modified.value,
            )
    }
}
