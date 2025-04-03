package com.example.cafelabservice.utils

import com.example.cafelabservice.entity.User
import com.example.cafelabservice.security.SecurityUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.util.*

object JwtUtil {
    private val expirationTime = 1000 * 60 * 60 * 10  // 10 hours
    private val secretKey = "jW8L1OCUu0RwcAbpaDtEkAkC/dsvrh9E5P5Wkx7y/7ZExAX5ovuoyr0mdkQbM20k"
    /*fun generateToken(user: User): String {
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
    }*/

    fun generateToken(userDetails: SecurityUser): String {
        return Jwts.builder()
            .setSubject(userDetails.username)
            .claim("roles", userDetails.authorities)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUserDetails(token: String): Map<String, Any?> {
        val claims = Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .body

        return mapOf(
            "username" to claims.subject,
            "roles" to claims["roles"]
        )
    }

    private fun getSignKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractUsername(token: String): String? {
        return extractClaim(token, Claims::getSubject)
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .body
        return claimsResolver(claims)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractClaim(token, Claims::getExpiration).before(Date())
    }
}