package com.example.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.websocket.server.PathParam
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class BackendApplication

@RestController
class HelloController {
    @GetMapping("/")
    fun getHello(): String {
        return "Hello world!"
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
