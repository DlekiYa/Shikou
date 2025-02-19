package com.example.backend.repository.data

import jakarta.persistence.*

@Entity
data class WorkspaceUser(
    @OneToOne
    val user: UserAccount,
    @ManyToOne
    val workspace: Workspace,
    val role: WorkspaceRole
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

enum class WorkspaceRole {
    READER,
    WRITER,
    OWNER
}