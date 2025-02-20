package com.example.backend.service

import com.example.backend.exception.UserAlreadyCreatedException
import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.UserAccount
import com.example.backend.security.CustomUserDetailsService
import com.example.backend.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
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

    fun createUser(username: String, passwordRaw: String, authorities: List<String>) {
        userRepository.findByUsername(username) ?: throw UserAlreadyCreatedException()
        UserAccount(username, encoder.encode(passwordRaw), authorities)
    }

    fun getTokenFromExistingUser(username: String, passwordRaw: String): String {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, passwordRaw)
        )
        return auth.name
    }
}