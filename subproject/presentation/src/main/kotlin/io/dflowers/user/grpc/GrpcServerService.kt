package io.dflowers.user.grpc

import com.google.protobuf.Timestamp
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcServerService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {
    override suspend fun getUserById(request: GetUserByIdRequest): GetUserByIdResponse =
        GetUserByIdResponse
            .newBuilder()
            .setAge(5)
            .setName("test")
            .setId(request.id!!)
            .setCreated(Timestamp.newBuilder())
            .build()
}
