package io.dflowers.user.graphql.resolver

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.toSchema
import com.expediagroup.graphql.server.operations.Query
import io.dflowers.user.graphql.schema.UserSchema
import org.springframework.stereotype.Component

@Component
class UserQuery : Query {
    @GraphQLDescription("Get a user by id")
    suspend fun user(id: String): UserSchema =
        UserSchema(
            id = id,
            email = "sattlub123@gmail.com",
            name = "최민규",
            created = 5,
            modified = 5,
        )
}

val config =
    SchemaGeneratorConfig(
        supportedPackages = listOf("io.dflowers.user"),
    )
val schema =
    toSchema(
        config = config,
        queries =
            listOf(
                TopLevelObject(UserQuery()),
            ),
    )
