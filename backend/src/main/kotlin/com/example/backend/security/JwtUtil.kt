package com.example.backend.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


@Service
class JwtUtil {
    private final val SECRET = "TUlJRXZnSUJBREFOQmdrcWhraUc5dzBCQVFFRkFBU0NCS2d3Z2dTa0FnRUFBb0lCQVFDeWVlOU5iS1V3UVBIQgpIMlNKTk1aSHVjYkFZQXFuZmRWeHpiYUhRT2tSdTNEcHpwMGxlWG05MDhPSm11ZXI2Q01mb1hISHI3SGtoc0dFCjhTNlNSKytQaC80dUI4dDdUODVLK09iWk9waXlvYVgrbEpOUDhNK2lYcnh4YzJIUVJBck8yQzBTNzJWVWJYTDUKQUhHWjNSSDV1ZlpEWTlTc2hQSE9jYzk3VlRacndiTmtVeWZDVmNjQjRSdWFkNldjWGQxanphS2xuVndtc0twaApTRndCQlZuUE1UY0tsMUlLTFVWUW9UZzlqaVFuVTduaFlJRUZRYlgwTGhqbzlDMXE2QzBDMjhKWU5QYnE2VGZqCmxHN0tDRkE3OWxXZE1zaG0xRjA5K09pQ2paSjRPTUllQUM3Nk9nWDFmdUphL2xlTG9kUHFVN3hMYngyNXY2cUwKSkNyek1DUWpBZ01CQUFFQ2dnRUFTemFVc1FHNHFabXM1L0NWV3pjWTJsQkJvSCs0N2JYYlZnN1Z2YkVPcDVEZworRDlBdlhLd2FkMk11WGhFNm82MUFwYjBUcTFOb0J0a0FXVHNkZXdIMm1wNnBTWld5N3dwbVRIRWcwWXM1SmEzCjBBNEgrTGZvb0tvZzZ3U3ZjMmZCVUVNTTE3NTRhdmlHNjhXU21qOXVYRm5Rbkx5OCt3azVxQUVSYVkzVEZkeUMKZG9DWVVkNENiMkpJckJ6cUJYWG15V25sNjZyR2FLd0hiYkxUbGNwZXM0ZjRiTUM4dTRTaTIwbW9jUlBvalBwSQozcHkvYmJUWnNkblhvU0VkZ1RHaDgyQ0I2SmYrYi9tVDIxaGZhMXZsRnFaa0kybC9XSkZjaVE4R09HRVhnUHlOCnVjdGtqbXUyS3kwc09oVUdya2lYWkM2RHd1L1pLWkJqMlRlbHBaYWIzUUtCZ1FEai9pWVVUSU1IN3hKd1p6OUcKanFLT25SYUdadFlSaGZjZysyV1ZvYmVqWnBGbXYvRjkvaHdqc1d1anB5eGlvNGh2d0YwSU00UE9LcE1zZmRWcwpDeTFYTVY4V05DVVVxNFNoMVFTeitlekJxOVVLa1pCMmxjd3EzcU1jSDBiMlNJWXllem9RRWFTc2pMOHg5ZnptCm5MYlkvanZESE9QcWwzQjlxcmx3c0tBREp3S0JnUURJWnB0SFVSMUZyU2puUlE3UkE2emtVNDdydkFvNlhHZzEKVUwvejVGbEZiKzFBenNjcU9ZVDRGQXJLdWZIQVB2b1JWNnd5cVZxQ05IMzFKVVpiUjlucDBRQXh3bVRZTWczeAp3SzRQZ2ZPdnBoa2VwZVkxaHNlU1FOTS94V1E1NHVJdkdvdExvM2NyQndML2RCSCtVQStKTzh6SVZYYkJkbTVUCk9OdkkzdUtFcFFLQmdRQ0Z6WndQZ2EvTXNqUUNIN1gwOUxUV09iOEVkY25nVXR6c1VGNFg2T2JiVU5DNG1xRmgK"
    private final val EXPIRATION_TIME = 900_000

    fun extractUsername(token: String): String {
        return extractClaim(token, { obj: Claims -> obj.subject })
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, { obj: Claims -> obj.expiration })
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(SECRET).build().parseClaimsJws(token).getBody()
    }

    fun generateToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact()
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token)
    }
}