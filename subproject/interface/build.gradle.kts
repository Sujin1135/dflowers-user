import com.google.protobuf.gradle.ProtobufPlugin
import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.jlleitschuh.ktlint)
}

sourceSets {
    main {
        proto {
            srcDirs("./dflowers-user-interface")
            include("**/*.protodevel")
        }
        java {
            srcDirs(
                "build/generated/source/proto/main/grpc",
                "build/generated/source/proto/main/grpckt",
                "build/generated/source/proto/main/java",
                "build/generated/source/proto/main/kotlin",
            )
        }
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        filter {
            exclude {
                it.file.path.startsWith(
                    project.layout.buildDirectory
                        .get()
                        .dir("generated")
                        .toString(),
                )
            }
        }
    }
}

allprojects {
    apply<ProtobufPlugin>()

    apply(plugin = "com.google.protobuf")
    apply(plugin = "java")

    dependencies {
        implementation(libs.javax.annotation.api)

        api(libs.bundles.grpc.presentation)
    }

    protobuf {
        // Protobuf 컴파일러를 지정하여 .proto 파일을 컴파일합니다.
        protoc {
            artifact = "com.google.protobuf:protoc:3.22.2"
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
