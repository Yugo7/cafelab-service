package com.example.cafelabservice.service

import com.example.cafelabservice.entity.PasswordToken
import com.example.cafelabservice.entity.User
import com.example.cafelabservice.repositories.PasswordTokenRepository
import com.example.cafelabservice.repositories.UserRepository
import com.example.cafelabservice.utils.JwtUtil
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordTokenRepository: PasswordTokenRepository,
    private val emailService: EmailService
) {

    fun getAllUsers() = userRepository.findAll()

    fun getUserById(id: Long) = userRepository.findById(id)

    fun getUserByUsername(username: String) = userRepository.findByUsername(username)

    fun createUser(user: User) = userRepository.save(user)

    fun updateUser(id: Long, newUser: User) {
        userRepository.findById(id).map { existingUser ->
            val updatedUser: User = existingUser
                .copy(
                    email = newUser.email,
                    address = newUser.address
                )
            userRepository.save(updatedUser)
        }
    }

    fun deleteUser(id: Long) = userRepository.deleteById(id)

    fun requestPasswordReset(email: String) {
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("User not found")
        val token = generateRandomToken()
        val expiresAt = LocalDateTime.now().plusHours(1)
        passwordTokenRepository.save(PasswordToken(user = user, token = token, expiresAt = expiresAt))
        emailService.sendPasswordResetEmail(user.email, token)
    }

    private fun generateRandomToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun findAllEmails(): List<String> {
        return userRepository.findAll().map { it.email }
    }

    fun signInUser(email: String, password: String): String? {
        val user = userRepository.findByEmail(email) ?: return null
        return if (BCrypt.checkpw(password, user.password)) {
            //JwtUtil.generateToken(user)
            println("Sign in errado")
            null
        } else {
            null
        }
    }

    fun resetPassword(token: String, password: String): Any? {
        return TODO("Provide the return value")
    }
}