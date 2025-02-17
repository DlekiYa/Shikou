package com.example.backend.repository

import com.example.backend.repository.data.UserAccount
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserAccount, Long> {
    fun findByUsername(username: String?): UserAccount?
}