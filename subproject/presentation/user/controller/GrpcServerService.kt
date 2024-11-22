package io.dflowers.user.controller

import io.dflowers.user.grpc.GetUserByIdRequest
import io.dflowers.user.grpc.GetUserByIdResponse
import io.dflowers.user.grpc.UserServiceGrpcKt

class GrpcServerService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {
    override suspend fun getUserById(request: GetUserByIdRequest): GetUserByIdResponse {
        return super.getUserById(request)
    }
}
