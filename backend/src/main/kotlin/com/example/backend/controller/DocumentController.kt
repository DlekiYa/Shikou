package com.example.backend.controller


import com.example.backend.service.DocumentService
import com.example.backend.exception.DocumentNotFoundException
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
@RequestMapping("/api/document")
class DocumentController(
    private val documentService: DocumentService
) {
    @PostMapping("/create")
    fun createDocument(@RequestBody @Valid request: CreateDocumentRequest): ResponseEntity<StatusResponse> {
        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
            request.authorities != null) {
            authorities.addAll(request.authorities)
        }

        documentService.createDocument(request.workspace, request.path, request.title)

        return ResponseEntity.ok(StatusResponse("success"))
    }

    /*
    @GetMapping("/{id}")
    fun getDocument(@RequestBody @Valid request: getByIdRequest): String {
        val authorities: MutableList<String> = mutableListOf("ROLE_USER")
        if (SecurityContextHolder.getContext().getAuthentication()?.authorities?.firstOrNull { it.authority == "ROLE_ADMIN" } != null &&
            request.authorities != null) {
            authorities.addAll(request.authorities)
        }
        val document = documentService.findById(request.id).getOrNull() ?: throw DocumentNotFoundException()
        return document
    } */
}

data class CreateDocumentRequest(
    @field:NotBlank
    val path: String,
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val workspace: String,
    val authorities: List<String>?
)

data class getByIdRequest(
    @field:NotBlank
    val id: Integer,
    val authorities: List<String>?
)
