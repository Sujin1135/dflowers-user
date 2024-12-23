package io.dflowers.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class UserApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application,application-infrastructure,application-app")
    BlockHound
        .builder()
        .allowBlockingCallsInside("org.springframework.security.crypto.password.PasswordEncoder", "encode")
        .install()
    runApplication<UserApplication>(*args)
}
