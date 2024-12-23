package io.dflowers.user.exception

import io.dflowers.user.dto.ErrorCode
import java.time.LocalDateTime

sealed class HttpException(
    open val timestamp: LocalDateTime,
    open val code: ErrorCode,
    override val message: String,
) : RuntimeException(message) {
    data class NotFound(
        override val timestamp: LocalDateTime,
        override val code: ErrorCode,
        val objectName: String,
        val objectId: String,
    ) : HttpException(timestamp, code, "$objectName($objectId) was not found") {
        companion object {
            fun create(
                code: ErrorCode = ErrorCode.NOT_FOUND,
                objectName: String,
                objectId: String,
            ) = NotFound(
                timestamp = LocalDateTime.now(),
                code = code,
                objectName = objectName,
                objectId = objectId,
            )
        }
    }

    data class BadRequest(
        override val timestamp: LocalDateTime,
        override val code: ErrorCode,
        override val message: String,
    ) : HttpException(timestamp, code, message) {
        companion object {
            fun create(
                code: ErrorCode,
                message: String,
            ) = BadRequest(
                timestamp = LocalDateTime.now(),
                code = code,
                message = message,
            )
        }
    }

    data class Unauthorized(
        override val timestamp: LocalDateTime,
        override val code: ErrorCode,
        override val message: String,
    ) : HttpException(timestamp, code, message) {
        companion object {
            fun create(
                code: ErrorCode,
                message: String,
            ) = Unauthorized(
                timestamp = LocalDateTime.now(),
                code = code,
                message = message,
            )
        }
    }
}
