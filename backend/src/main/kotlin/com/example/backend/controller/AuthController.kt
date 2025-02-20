package com.example.backend.controller

import com.example.backend.service.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun registerUser(@RequestBody @Valid request: AuthRequest): ResponseEntity<AuthResponse> {
//        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
//        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
//            request.authorities != null) {
//            authorities.addAll(request.authorities)
//        }

        authService.createUser(request.username, request.password, emptyList())
        return ResponseEntity.ok(AuthResponse(authService.getTokenFromExistingUser(request.username, request.password)))
    }

    @PostMapping("/signin")
    fun loginUser(@RequestBody @Valid request: AuthRequest): ResponseEntity<AuthResponse> {
        val token: String
        try {
            token = authService.getTokenFromExistingUser(request.username, request.password)
        } catch (ex: BadCredentialsException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials")
        }

        return ResponseEntity.ok(AuthResponse(token))
    }
}

data class AuthRequest(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val password: String,
    val authorities: List<String>?
)

data class AuthResponse(
    val token: String?
)