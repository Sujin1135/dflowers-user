package io.dflowers.user.handler

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import io.dflowers.user.dto.ErrorCode
import io.dflowers.user.exception.HttpException
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class GraphQLExceptionHandler : DataFetcherExceptionHandler {
    override fun handleException(
        handlerParameters: DataFetcherExceptionHandlerParameters,
    ): CompletableFuture<DataFetcherExceptionHandlerResult> {
        if (handlerParameters.exception is HttpException) {
            val ex: HttpException = handlerParameters.exception as HttpException

            val graphqlError: GraphQLError =
                TypedGraphQLError
                    .newInternalErrorBuilder()
                    .message(ex.message)
                    .errorType(ex.extractErrorType())
                    .path(handlerParameters.path)
                    .build()

            val result =
                DataFetcherExceptionHandlerResult
                    .newResult()
                    .error(graphqlError)
                    .build()

            return CompletableFuture.completedFuture(result)
        } else {
            return DefaultDataFetcherExceptionHandler().handleException(handlerParameters)
        }
    }

    fun HttpException.extractErrorType() =
        when (this.code) {
            ErrorCode.INVALID_TOKEN -> ErrorType.UNAUTHORIZED
            ErrorCode.NOT_FOUND -> ErrorType.NOT_FOUND
            ErrorCode.WRONG_CREDENTIALS -> ErrorType.UNAUTHORIZED
            ErrorCode.ALREADY_REGISTERED -> ErrorType.BAD_REQUEST
        }
}
