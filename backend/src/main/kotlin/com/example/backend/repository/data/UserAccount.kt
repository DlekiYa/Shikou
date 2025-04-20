package com.example.backend.repository.data

import jakarta.persistence.*

@Entity
data class UserAccount(
    @Column(unique = true)
    val username: String,
    val password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val authorities: List<String> = emptyList(),

    @OneToMany
    val workspaces: List<Workspace> = emptyList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}