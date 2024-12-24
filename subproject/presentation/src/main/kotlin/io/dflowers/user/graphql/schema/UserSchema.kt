package io.dflowers.user.graphql.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription("App user")
data class UserSchema(
    val id: String,
    val email: String,
    val name: String,
    val created: Int,
    val modified: Int,
)
