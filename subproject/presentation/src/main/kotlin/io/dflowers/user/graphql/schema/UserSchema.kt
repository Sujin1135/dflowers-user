package io.dflowers.user.graphql.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.time.LocalDateTime

@GraphQLDescription("App user")
data class UserSchema(
    val id: String,
    val email: String,
    val name: String,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)
