package com.example.cafelabservice.service

import com.example.cafelabservice.entity.PasswordToken
import com.example.cafelabservice.entity.User
import com.example.cafelabservice.models.dto.UserBalanceDTO
import com.example.cafelabservice.models.dto.UserSummaryDTO
import com.example.cafelabservice.models.dto.toOrderResponseDTO
import com.example.cafelabservice.models.dto.toSubscriptionResponseDTO
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
    private val emailService: EmailService,
    private val orderService: OrderService,
    private val productService: ProductService,
    private val stripeService: StripeService
) {

    fun getAllUsers() = userRepository.findAll()

    fun getUserById(id: Long) = userRepository.findById(id)

    fun getUserByUsername(username: String) = userRepository.findByUsername(username)

    fun getUserFromToken(token: String): User {
        val username = JwtUtil.extractUsername(token) ?: throw RuntimeException("Invalid token")
        return userRepository.findByUsername(username) ?: throw RuntimeException("User not found")
    }

    fun getAuthenticatedUserInfo(user: User): UserSummaryDTO {
        val orders = orderService.getOrdersByUser(user.id).map { it.toOrderResponseDTO(productService) }
        val subscriptions = orderService.getSubscriptionsByUser(user.id)?.mapNotNull { it.subscription?.toSubscriptionResponseDTO(it.order) }
        return UserSummaryDTO(
            name = user.name ?: user.username ?: "",
            email = user.email,
            orders = orders,
            subscriptions = subscriptions,
            balance = user.stripeId?.let { getUserBalance(it) }
        )
    }

    fun createUser(user: User) = userRepository.save(user)

    fun updateUser(id: Long, newUser: User): User {
        val existingUser = userRepository.findById(id)
            .orElseThrow { RuntimeException("User with ID $id not found") }

        val updatedUser = existingUser.copy(
            email = newUser.email,
            address = newUser.address
        )

        return userRepository.save(updatedUser)
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

    fun resetPassword(token: String, password: String) {
        val passwordToken = passwordTokenRepository.findByToken(token)
            ?: throw IllegalArgumentException("Invalid token")

        require(passwordToken.expiresAt.isAfter(LocalDateTime.now())) {
            throw IllegalArgumentException("Token expired")
        }

        val updatedUser = passwordToken.user.copy(password = BCrypt.hashpw(password, BCrypt.gensalt()))
        userRepository.save(updatedUser)
    }

    fun getUserBalance(stripeId: String): UserBalanceDTO {
        val balance = stripeService.getCustomerBalance(stripeId)
        return UserBalanceDTO(
            balance = balance.toString(),
            expiration = null,
        )
    }
}