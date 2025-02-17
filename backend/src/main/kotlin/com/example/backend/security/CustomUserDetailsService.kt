package com.example.backend.security

import com.example.backend.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(val userRepo : UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?) : UserDetails {
        val user = userRepo.findByUsername(username) ?: throw UsernameNotFoundException("User $username not found")
        return User(user.username, user.password, user.authorities.map { elem -> GrantedAuthority { elem } })
    }
}