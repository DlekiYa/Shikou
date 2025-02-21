package com.example.backend

import com.example.backend.repository.UserRepository
import com.example.backend.repository.data.UserAccount
import com.example.backend.service.AuthService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import jakarta.validation.constraints.NotEmpty
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import javax.print.attribute.standard.Media
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class AuthMockMvcTests {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }

    @Test
    fun createUserTest() {
        mockMvc.post("/api/auth/signup") {
            contentType = MediaType.APPLICATION_JSON
            content = """{
                |"username": "alice",
                |"password": "password"
                |}""".trimMargin()
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.token") { isNotEmpty() } }
        }
    }

    @Test
    fun loginUserTest() {
        val user = UserAccount("alice", passwordEncoder.encode("password"))
        userRepository.save(user)

        mockMvc.post("/api/auth/signin") {
            contentType = MediaType.APPLICATION_JSON
            content = """{
                |"username": "alice",
                |"password": "password"
            }""".trimMargin()
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.token") { isNotEmpty() } }
        }
    }

    @Test
    fun authorizationSuccessTest() {
        val user = UserAccount("alice", passwordEncoder.encode("password"))
        userRepository.save(user)
        val token = authService.getTokenFromExistingUser("alice", "password")

        mockMvc.get("/api/user/alice") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.username") { value("alice") } }
        }
    }

    @Test
    fun authorizationFailedWithIncorrectSignatureTest() {
        val user = UserAccount("alice", passwordEncoder.encode("password"))
        userRepository.save(user)
        val tokenSplit = authService.getTokenFromExistingUser("alice", "password").split(".").toMutableList()
        tokenSplit.removeLast()
        tokenSplit.addLast("incorrect_signature")
        val token = tokenSplit.joinToString(".")
        println(token)

        mockMvc.get("/api/user/alice") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isForbidden() }
        }
    }
}