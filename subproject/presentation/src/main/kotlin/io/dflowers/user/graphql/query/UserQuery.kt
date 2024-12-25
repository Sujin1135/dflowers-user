package io.dflowers.user.graphql.query

import arrow.core.raise.get
import arrow.core.raise.mapError
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import io.dflowers.user.entity.User
import io.dflowers.user.exception.HttpException
import io.dflowers.user.graphql.schema.UserSchema
import io.dflowers.user.service.FindOneUser
import org.springframework.stereotype.Component

@Component
class UserQuery(
    private val findOneUser: FindOneUser,
) : Query {
    @GraphQLDescription("Get a user by id")
    suspend fun user(email: String): UserSchema =
        UserSchema.from(
            findOneUser(User.Email(email))
                .mapError {
                    when (it) {
                        FindOneUser.Failure.NotFoundUser -> throw HttpException.NotFound.create(
                            objectName = User::class.simpleName!!,
                            objectId = email,
                        )
                    }
                }.get(),
        )
}
