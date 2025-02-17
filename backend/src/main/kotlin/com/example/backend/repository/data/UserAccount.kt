package com.example.backend.repository.data

import jakarta.persistence.*

@Entity
data class UserAccount(
    @Column(unique = true)
    val username: String,
    val password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val authorities: List<String>

    //@ManyToMany
    //val workspaces: List<Workspace>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}