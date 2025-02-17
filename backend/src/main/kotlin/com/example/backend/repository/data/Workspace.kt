package com.example.backend.repository.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
data class Workspace(
    val name: String,
    val description: String,

    //@ManyToMany(mappedBy = "workspaces")
    //val users: List<UserAccount>
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}