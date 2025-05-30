package com.example.backend.controller

import com.example.backend.service.WorkspaceService
import com.example.backend.repository.data.Workspace
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/workspace")
class WorkspaceController(
    private val workspaceService: WorkspaceService
) {
    @PostMapping("/create")
    fun createWorkspace(@RequestBody @Valid request: CreateWorkspaceRequest): ResponseEntity<StatusResponse> {
        val ws : Workspace = workspaceService.createWorkspace(request.name);
        workspaceService.addWorkspaceToUser(request.username, ws);
        return ResponseEntity.ok(StatusResponse("success"))
    }

    @GetMapping("/{id}")
    fun getWorkspace(@PathVariable id: Long): ResponseEntity<StatusResponse> {
        return ResponseEntity.ok(StatusResponse(workspaceService.getWorkspace(id.toString())))
    }
}

data class CreateWorkspaceRequest(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val name: String,
)

//data class GetWorkspaceRequest(
//    @field:NotBlank
//    val id: String,
//    val authorities: List<String>?
//)

data class StatusResponse(
    @field:NotBlank
    val status: String
)