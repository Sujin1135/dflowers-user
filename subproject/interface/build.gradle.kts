import com.google.protobuf.gradle.ProtobufPlugin
import com.google.protobuf.gradle.id

plugins {
    id("java-library")
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    api(project(":subproject:interface:dflowers-user-interface"))
}

allprojects {
    apply<ProtobufPlugin>()

    apply(plugin = "com.google.protobuf")
    apply(plugin = "java")

    dependencies {
        implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
        implementation("io.grpc:grpc-netty-shaded:1.68.1")
        implementation("io.grpc:grpc-protobuf:1.68.1")
        implementation("io.grpc:grpc-stub:1.68.1")
        implementation("io.grpc:grpc-kotlin-stub:1.3.0")
        implementation("com.google.protobuf:protobuf-java-util:3.22.2")
        implementation("com.google.protobuf:protobuf-java:3.22.2")
        implementation("com.google.protobuf:protobuf-kotlin:3.22.2")
    }

    protobuf {
        // Protobuf 컴파일러를 지정하여 .proto 파일을 컴파일합니다.
        protoc {
            artifact = "com.google.protobuf:protoc:0.9.4"
        }
        // gRPC 플러그인을 설정하여 Protobuf 파일로부터 gRPC 관련 코드를 생성합니다.
        plugins {
            id("grpc") {
                artifact = "io.grpc:protoc-gen-grpc-java:1.68.1"
            }
            id("grpckt") {
                artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
            }
        }
        // 모든 프로토콜 버퍼 작업에 대해 gRPC 플러그인을 적용합니다.
        generateProtoTasks {
            all().forEach { task ->
                task.plugins {
                    id("grpc")
                    id("grpckt")
                }
                task.builtins {
                    id("kotlin")
                }
            }
        }
    }
}
