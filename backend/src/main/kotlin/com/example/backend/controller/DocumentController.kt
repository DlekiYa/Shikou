package com.example.backend.controller


import com.example.backend.configuration.DocumentConfiguration
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
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/document")
class DocumentController(
    private val documentService: DocumentService,
    private val documentRepository: DocumentRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val documentConfiguration: DocumentConfiguration
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
    fun getDocument(@PathVariable id: Long): Document {
        val document = documentRepository.findById(id).getOrNull() ?: throw DocumentNotFoundException()
        return document
    }

    @PostMapping("/path")
    fun getDocument(@RequestBody @Valid request: GetDocumentRequest): ResponseEntity<Document> {
        val documents = documentRepository.findDocumentsByPath(request.path)
        return ResponseEntity.ok(documents.first())
    }

    @GetMapping("/raw/{id}")
    fun getDocumentRaw(@PathVariable id: Long): ByteArray {
        val document = documentRepository.findById(id).getOrNull() ?: throw DocumentNotFoundException()
        val fullPath = Paths.get("${documentConfiguration.workspaceRootDirectory.removeSuffix("/")}/${document.workspace.id}/${document.path}")
        if (!fullPath.isRegularFile()) {
            throw RuntimeException("$fullPath is not a regular file")
        }
        return fullPath.readBytes()
    }

    @PostMapping("/raw/{id}")
    fun saveDocumentRaw(@PathVariable id: Long, @RequestBody data: ByteArray): ResponseEntity<Any> {
        val document = documentRepository.findById(id).getOrNull() ?: throw DocumentNotFoundException()
        val fullPath =
            Paths.get("${documentConfiguration.workspaceRootDirectory.removeSuffix("/")}/${document.workspace.id}/${document.path}")
        if (!fullPath.isRegularFile()) {
            throw RuntimeException("$fullPath is not a regular file")
        }
        fullPath.writeBytes(data)
        return ResponseEntity.ok(null)
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

data class GetDocumentRequest(
    @field:NotBlank
    val path: String,
    val workspaceId: Long = -1
)