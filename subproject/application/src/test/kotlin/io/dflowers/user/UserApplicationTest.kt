package io.dflowers.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserApplicationTest

fun main(args: Array<String>) {
    runApplication<UserApplicationTest>(*args)
}
