package com.example.backend.controller

import com.example.backend.exception.DocumentNotFoundException
import com.example.backend.repository.DocumentRepository
import com.example.backend.repository.data.Document
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/document")
class DocumentController(
    private val documentRepository: DocumentRepository
) {
    @GetMapping("/{id}")
    fun getDocument(@PathVariable id: Long): Document {
        val document = documentRepository.findById(id).getOrNull() ?: throw DocumentNotFoundException()
        return document
    }
}