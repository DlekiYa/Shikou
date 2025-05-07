package com.example.backend.service

import com.example.backend.repository.data.Workspace
import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.UserAccount
import com.example.backend.security.CustomUserDetailsService
import com.example.backend.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File


@Service
class DocumentService(
    private val authenticationManager: AuthenticationManager,
    private val userService: CustomUserDetailsService,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

fun createDocument(workspace: String, path: String, name: String) {
    val fullPath = "C:/workspaces/${workspace.removeSuffix("/")}/${path.removePrefix("/").removeSuffix("/")}/$name"
    
    val file = File(fullPath)
    
    file.parentFile?.mkdirs()
    
    file.createNewFile()
}
}