package com.afi.rewarder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RewarderApplication

fun main(args: Array<String>) {
    runApplication<RewarderApplication>(*args)
}
