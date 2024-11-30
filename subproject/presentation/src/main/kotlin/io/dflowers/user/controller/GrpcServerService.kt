package io.dflowers.user.controller

import io.dflowers.user.grpc.GetUserByIdRequest
import io.dflowers.user.grpc.GetUserByIdResponse
import io.dflowers.user.grpc.UserServiceGrpcKt
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcServerService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {
    override suspend fun getUserById(request: GetUserByIdRequest): GetUserByIdResponse {
        return super.getUserById(request)
    }
}
