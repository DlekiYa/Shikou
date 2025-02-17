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

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: CustomUserDetailsService,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/signup")
    fun registerUser(@RequestBody @Valid request: AuthRequest): ResponseEntity<UserAccount> {
        val user = userRepository.findByUsername(request.username)
        if (user != null) {
            return ResponseEntity.badRequest().build()
        }
        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
            request.authorities != null) {
            authorities.addAll(request.authorities)
        }

        val savedUser = UserAccount(username = request.username, password = encoder.encode(request.password), authorities = authorities)
        val ret = userRepository.save(savedUser)
        return ResponseEntity(ret, HttpStatus.CREATED)
    }

    @PostMapping("/signin")
    fun loginUser(@RequestBody @Valid request: AuthRequest): ResponseEntity<AuthResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (ex: BadCredentialsException) {
            throw InvalidCredentialsException()
        }

        val userDetails = userService.loadUserByUsername(request.username)
        val token = jwtUtil.generateToken(userDetails.username)
        return ResponseEntity.ok(AuthResponse(token = token))
    }
}

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Invalid credentials")
class InvalidCredentialsException : RuntimeException() {}

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