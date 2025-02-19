package com.example.backend.repository.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany

@Entity
data class Workspace(
    val name: String,
    val description: String,

    @OneToMany
    val documents: List<Document> = emptyList(),

    @OneToMany
    val users: List<WorkspaceUser> = emptyList()
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}