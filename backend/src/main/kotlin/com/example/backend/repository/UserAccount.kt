package com.example.backend.repository

import jakarta.persistence.*

@Entity
data class UserAccount(
    @Column(unique = true)
    val username: String,
    val password: String,
    val bio: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}