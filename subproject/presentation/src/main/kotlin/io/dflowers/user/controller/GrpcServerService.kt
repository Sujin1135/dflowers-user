package io.dflowers.user.controller

import com.google.protobuf.Timestamp
import io.dflowers.user.grpc.GetUserByIdRequest
import io.dflowers.user.grpc.GetUserByIdResponse
import io.dflowers.user.grpc.UserServiceGrpcKt
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcServerService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {
    override suspend fun getUserById(request: GetUserByIdRequest): GetUserByIdResponse {
        return GetUserByIdResponse.newBuilder()
            .setAge(5)
            .setName("test")
            .setId(request.id!!)
            .setCreated(Timestamp.newBuilder())
            .build()
    }
}
