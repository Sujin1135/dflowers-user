package io.dflowers.user.service

import io.dflowers.user.config.FlywayConfig
import io.dflowers.user.config.JooqConfig
import io.dflowers.user.config.R2dbcConfig
import io.dflowers.user.repository.UserRepository
import io.dflowers.user.repository.UserRepositoryImpl
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["spring.r2dbc.initialization-mode=always"])
@Import(
    JooqConfig::class,
    FlywayConfig::class,
    R2dbcConfig::class,
)
@ContextConfiguration
@SpringBootTest(classes = [FindOneUser::class, UserRepository::class, UserRepositoryImpl::class])
class FindOneUserTest(
    private val findOneUser: FindOneUser,
) : FreeSpec({
        extension(SpringExtension)

        "add" - {
            1 + 3 shouldBe 4
        }
    })
