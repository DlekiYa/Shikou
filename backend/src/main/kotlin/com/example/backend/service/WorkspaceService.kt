package com.example.backend.service

import com.example.backend.repository.WorkspaceRepository
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

@Service
class WorkspaceService(
    private val authenticationManager: AuthenticationManager,
    private val userService: CustomUserDetailsService,
    private val workspaceRepository: WorkspaceRepository,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    fun createWorkspace(username: String, name: String) {
        val new_workspace = Workspace(name, "");
        workspaceRepository.save(new_workspace);
    }

    @Transactional
    fun addWorkspaceToUser(username: String, workspace: Workspace): UserAccount {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")
        
        // Create a new mutable list to avoid immutability issues
        val updatedWorkspaces = user.workspaces.toMutableList().apply {
            add(workspace)
        }
        
        // Since workspaces is a val, we need to handle this differently
        // This requires adding a method to update workspaces in UserAccount
        user.updateWorkspaces(updatedWorkspaces)
        
        return userRepository.save(user)
    }
}