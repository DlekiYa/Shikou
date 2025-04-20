package com.example.backend

import com.example.backend.exception.UserAlreadyCreatedException
import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.UserAccount
import com.example.backend.service.AuthService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(MockKExtension::class)
@ExtendWith(SpringExtension::class)
class AuthServiceTests() {
    @MockkBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var encoder: PasswordEncoder

    @Test
    fun loginUserWithNonExistingUsernameThrowsBadCredentialsException() {
        every { userRepository.findByUsername("alice") } returns(null)
        assertThrows<BadCredentialsException> {
            authService.getTokenFromExistingUser("alice", "password")
        }
        verify { userRepository.findByUsername("alice") }
    }

    @Test
    fun loginUserWithBadPasswordThrowsBadCredentialsException() {
        every { userRepository.findByUsername("alice") } returns(UserAccount("alice", encoder.encode("password")))
        assertThrows<BadCredentialsException> {
            authService.getTokenFromExistingUser("alice", "invalid")
        }
        verify { userRepository.findByUsername("alice") }
    }

    @Test
    fun loginUserWithRightCredentialsShouldReturnToken() {
        every { userRepository.findByUsername("alice") } returns(UserAccount("alice", encoder.encode("password")))
        val token = authService.getTokenFromExistingUser("alice", "password")
        assertFalse(token.isEmpty())
        verify { userRepository.findByUsername("alice") }
    }

    @Test
    fun registerNonExistingUser() {
        every { userRepository.findByUsername("alice") } returns(null)
        every { userRepository.save(any()) } returns(UserAccount("alice", encoder.encode("password")))

        authService.createUser("alice", "password", emptyList())
        verify {
            userRepository.findByUsername("alice")
            userRepository.save(any())
        }
    }

    @Test
    fun registerExistingUser() {
        every { userRepository.findByUsername("alice") } returns(UserAccount("alice", encoder.encode("password")))
        assertThrows<UserAlreadyCreatedException> { authService.createUser("alice", "password", emptyList()) }
        verify { userRepository.findByUsername("alice") }
    }
}