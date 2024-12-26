package io.dflowers.user.graphql.config

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.generator.toSchema
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLType
import graphql.schema.idl.RuntimeWiring
import io.dflowers.user.graphql.query.UserQuery
import io.dflowers.user.service.FindOneUser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import java.time.OffsetDateTime
import kotlin.reflect.KType

@Configuration
class GraphqlConfig(
    private val findOneUser: FindOneUser,
) {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer =
        RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder ->
            wiringBuilder.scalar(
                ExtendedScalars.DateTime,
            )
        }

    @Bean
    fun graphQLSchema(): GraphQLSchema =
        toSchema(
            config =
                SchemaGeneratorConfig(
                    supportedPackages = listOf("io.dflowers.user"),
                    hooks =
                        CustomSchemaGeneratorHooks(),
                ),
            queries =
                listOf(
                    TopLevelObject(UserQuery(findOneUser)),
                ),
        )
}

class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when (type.classifier) {
            OffsetDateTime::class -> ExtendedScalars.DateTime
            else -> null
        }
}
