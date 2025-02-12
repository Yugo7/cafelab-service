package com.example.cafelabservice.utils

import com.example.cafelabservice.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*

object JwtUtil {
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun generateToken(user: User): String {
        with(user){
            return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("name", name)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(key)
                .compact()
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getEmailFromToken(token: String): String {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return claims.subject
    }
}