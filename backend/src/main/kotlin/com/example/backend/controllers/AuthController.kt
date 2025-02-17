package com.example.backend.controllers

import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.UserAccount
import com.example.backend.security.CustomUserDetailsService
import com.example.backend.security.JwtUtil
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.ErrorResponse
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: CustomUserDetailsService,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    private fun getToken(username: String): String {
        val userDetails = userService.loadUserByUsername(username)
        val token = jwtUtil.generateToken(userDetails.username)
        return token
    }
    @PostMapping("/signup")
    fun registerUser(@RequestBody @Valid request: AuthRequest): ResponseEntity<AuthResponse> {
        val user = userRepository.findByUsername(request.username)
        if (user != null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User already created")
        }
        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
            request.authorities != null) {
            authorities.addAll(request.authorities)
        }

        val savedUser = UserAccount(username = request.username, password = encoder.encode(request.password), authorities = authorities)
        userRepository.save(savedUser)
        return ResponseEntity.ok(AuthResponse(getToken(savedUser.username)))
    }

    @PostMapping("/signin")
    fun loginUser(@RequestBody @Valid request: AuthRequest): ResponseEntity<AuthResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (ex: BadCredentialsException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials", ex)
        }

        return ResponseEntity.ok(AuthResponse(token = getToken(request.username)))
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