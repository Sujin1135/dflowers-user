package io.dflowers.user.graphql.query

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import io.dflowers.user.graphql.schema.UserSchema
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserQuery : Query {
    @GraphQLDescription("Get a user by id")
    suspend fun user(id: String): UserSchema =
        UserSchema(
            id = id,
            email = "sattlub123@gmail.com",
            name = "최민규",
            created = LocalDateTime.now(),
            modified = LocalDateTime.now(),
        )
}
