package com.example.backend.controller

import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.UserAccount
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.parameters.P
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository
) {
    @GetMapping("/{username}")
    @PreAuthorize("authentication.name == #u || hasRole('ADMIN')")
    fun getUserByUsername(@PathVariable @P("u") username: String): UserAccount {
        val user = userRepository.findByUsername(username) ?: throw NotFoundException()
        return user
    }
}