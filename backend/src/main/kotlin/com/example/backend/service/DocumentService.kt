package com.example.backend.service

import com.example.backend.configuration.DocumentConfiguration
import com.example.backend.repository.DocumentRepository
import com.example.backend.repository.data.Workspace
import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.Document
import com.example.backend.repository.data.UserAccount
import com.example.backend.security.CustomUserDetailsService
import com.example.backend.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

@Service
class DocumentService(
    private val authenticationManager: AuthenticationManager,
    private val userService: CustomUserDetailsService,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val documentConfiguration: DocumentConfiguration,
    private val documentRepository: DocumentRepository
) {

    fun createDocument(workspace: Workspace, path: String, name: String) {
        val fullPath = Path("${documentConfiguration.workspaceRootDirectory}/${workspace.id}/${path.removePrefix("/").removeSuffix("/")}/$name")
        if (fullPath.exists()) {
            return;
        }

        val file = fullPath.toFile()
        file.parentFile?.mkdirs()
        file.createNewFile()

        val document = Document(workspace, fullPath.pathString)
        documentRepository.save(document)
    }
}