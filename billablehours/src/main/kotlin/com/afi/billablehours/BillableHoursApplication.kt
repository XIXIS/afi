package com.afi.billablehours

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class BillableHoursApplication

fun main(args: Array<String>) {
    runApplication<BillableHoursApplication>(*args)
}
