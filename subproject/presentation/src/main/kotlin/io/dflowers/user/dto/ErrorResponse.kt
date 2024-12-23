package io.dflowers.user.dto

import io.dflowers.user.exception.HttpException
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "에러 응답 객체")
data class ErrorResponse(
    @Schema(description = "에러 발생일시")
    val timestamp: LocalDateTime,
    @Schema(description = "에러 코드")
    val code: ErrorCode,
    @Schema(description = "에러 메시지")
    val message: String,
) {
    companion object {
        fun of(ex: HttpException) =
            ErrorResponse(
                timestamp = ex.timestamp,
                code = ex.code,
                message = ex.message,
            )
    }
}
