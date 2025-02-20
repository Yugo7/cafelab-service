package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.service.CustomUserDetailsService
import com.example.cafelabservice.utils.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class AuthRequest(val email: String, val password: String)
data class AuthResponse(val token: String)

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val userDetails = userDetailsService.loadUserByUsername(request.email)
        val jwt = JwtUtil.generateToken(userDetails)

        return ResponseEntity.ok(AuthResponse(jwt))
    }
}
