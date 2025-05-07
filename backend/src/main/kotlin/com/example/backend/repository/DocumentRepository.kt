package com.example.backend.repository

import com.example.backend.repository.data.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentRepository : JpaRepository<Document, Long> {
    fun findDocumentsByPath(path: String): List<Document>
}