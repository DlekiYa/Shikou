package com.example.backend.repository.data

import jakarta.persistence.*

@Entity
data class Document( //TODO: CHANGE THIS
    @ManyToOne
    val workspace: Workspace,
    //val role: WorkspaceRole,

    val path: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}