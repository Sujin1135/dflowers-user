package io.dflowers.user.service

import io.dflowers.user.UserApplicationTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
@SpringBootTest(classes = [UserApplicationTest::class])
class FindOneUserTest :
    FreeSpec({
        extension(SpringExtension)

        "add" - {
            1 + 3 shouldBe 4
        }
    })
