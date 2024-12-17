package io.dflowers.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class UserApplication

fun main(args: Array<String>) {
    BlockHound.install()
    runApplication<UserApplication>(*args)
}
