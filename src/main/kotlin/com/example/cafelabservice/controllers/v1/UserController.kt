package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.entity.User
import com.example.cafelabservice.models.dto.SignInRequestDTO
import com.example.cafelabservice.service.OrderService
import com.example.cafelabservice.service.ProductService
import com.example.cafelabservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val orderService: OrderService,
    private val productService: ProductService
) {

    @PostMapping
    fun createUser(@RequestBody customer: User): ResponseEntity<Any> {
        return try {
            val data = userService.createUser(customer)
            ResponseEntity.ok(data)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/forgot-password")
    fun requestChangePassword(@RequestBody request: ChangePasswordRequest): ResponseEntity<Any> {
        return try {
            val data = userService.requestPasswordReset(request.email)
            ResponseEntity.ok(data)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to e.message))
        }
    }


    @PostMapping("/change-password/{token}")
     fun resetPassword(@PathVariable token: String, @RequestBody request: PasswordChangeRequest): ResponseEntity<Any> {
        return try {
            val data = userService.resetPassword(token, request.password)
            ResponseEntity.ok(data)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/signin")
     fun signInUser(@RequestBody signInRequest: SignInRequestDTO): ResponseEntity<Any> {
        return try {
            val token = userService.signInUser(signInRequest.email, signInRequest.password)
            ResponseEntity.ok(token.toString())
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to e.message))
        }
    }

    @GetMapping
     fun getAllUsers(): ResponseEntity<Any> {
        return try {
            val users = userService.getAllUsers()
            ResponseEntity.ok(users)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("msg" to e.message))
        }
    }

    @GetMapping("/orders")
     fun getOrdersByUserId(@RequestHeader("Authorization") authorization: String): ResponseEntity<Any> {
        return try {
            val user = userService.getUserFromToken(authorization.removePrefix("Bearer "))
            return ResponseEntity.ok(userService.getAuthenticatedUserInfo(user))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("msg" to e.message))
        }
    }
}

data class PasswordChangeRequest(val password: String)

data class ChangePasswordRequest(val email: String)