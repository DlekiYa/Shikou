package com.example.backend.service

import com.example.backend.configuration.DocumentConfiguration
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
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.nio.file.Paths


@Serializable
data class FileEntry(
    val isDir: Boolean,
    val name: String,
    val children: List<FileEntry> = emptyList()
)

fun listDirectoryToJson(directoryPath: String): String {
    println(directoryPath)
    val rootDir = File(directoryPath)
    if (!rootDir.exists() || !rootDir.isDirectory) {
        return "[]"
    }

    fun buildDirectoryStructure(dir: File): List<FileEntry> {
        return dir.listFiles()?.map { file ->
            FileEntry(
                isDir = file.isDirectory,
                name = file.name,
                children = if (file.isDirectory) buildDirectoryStructure(file) else emptyList()
            )
        }?.toList() ?: emptyList()
    }

    return Json.encodeToString(buildDirectoryStructure(rootDir))
}


@Service
class WorkspaceService(
    private val workspaceRepository: WorkspaceRepository,
    private val userRepository: UserRepository,
    private val documentConfiguration: DocumentConfiguration
) {
    fun createWorkspace(name: String) : Workspace {
        val new_workspace = Workspace(name, "");

        val savedWorkspace = workspaceRepository.save(new_workspace);
        val workspacePath = Paths.get("${documentConfiguration.workspaceRootDirectory.removeSuffix("/")}/${savedWorkspace.id}")
        Files.createDirectory(workspacePath) // hope this works

        return new_workspace
    }

    @Transactional
    fun addWorkspaceToUser(username: String, workspace: Workspace) {
        println(username)
        val user = userRepository.findByUsername(username)
            ?: return
        
        // Create a new mutable list to avoid immutability issues
        val updatedWorkspaces = user.workspaces.toMutableList().apply {
            add(workspace)
        }
        
        // Since workspaces is a val, we need to handle this differently
        // This requires adding a method to update workspaces in UserAccount
        user.updateWorkspaces(updatedWorkspaces)
    }

    fun getWorkspace(workspaceId: String): String {
        return listDirectoryToJson( "${documentConfiguration.workspaceRootDirectory.removeSuffix("/")}/" + workspaceId)
    }
}