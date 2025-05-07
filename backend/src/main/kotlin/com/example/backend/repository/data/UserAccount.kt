package com.example.backend.repository.data

import jakarta.persistence.*

@Entity
data class UserAccount(
    @Column(unique = true)
    val username: String,
    val password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val authorities: List<String> = emptyList(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val workspaces: MutableList<Workspace> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    
    // Helper method to update workspaces
    fun updateWorkspaces(newWorkspaces: List<Workspace>) {
        this.workspaces.clear()
        this.workspaces.addAll(newWorkspaces)
    }
}