package com.example.backend.controller


import com.example.backend.service.DocumentService
import com.example.backend.exception.DocumentNotFoundException
import com.example.backend.repository.DocumentRepository
import com.example.backend.repository.WorkspaceRepository
import com.example.backend.repository.data.Document
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jdk.jshell.Snippet.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/document")
class DocumentController(
    private val documentService: DocumentService,
    private val documentRepository: DocumentRepository,
    private val workspaceRepository: WorkspaceRepository
) {
    // TODO: return document
    @PostMapping("/create")
    fun createDocument(@RequestBody @Valid request: CreateDocumentRequest): ResponseEntity<StatusResponse> {
        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
            request.authorities != null) {
            authorities.addAll(request.authorities)
        }

        val workspace = workspaceRepository.findById(request.workspaceId).getOrNull() ?: return ResponseEntity.notFound().build()

        //documentService.createDocument(request.workspace, request.path, request.title)
        documentService.createDocument(workspace, request.path, request.name)

        return ResponseEntity.ok(StatusResponse("success"))
    }

    @GetMapping("/{id}")
    fun getDocument(@RequestBody @Valid request: getByIdRequest): Document {
        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
            request.authorities != null) {
            authorities.addAll(request.authorities)
        }
        val document = documentRepository.findById(request.id).getOrNull() ?: throw DocumentNotFoundException()
        return document
    }
}

data class CreateDocumentRequest(
    @field:NotBlank
    val path: String,
    @field:NotBlank
    val name: String,
    val workspaceId: Long = -1,
    val authorities: List<String>?
)

data class getByIdRequest(
    @field:NotBlank
    val id: Long,
    val authorities: List<String>?
)
