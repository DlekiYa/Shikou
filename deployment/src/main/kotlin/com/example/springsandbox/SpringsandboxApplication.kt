package com.example.springsandbox

import org.apache.logging.log4j.util.EnvironmentPropertySource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment

@SpringBootApplication
class SpringsandboxApplication

fun main(args: Array<String>) {
    runApplication<SpringsandboxApplication>(*args)
}