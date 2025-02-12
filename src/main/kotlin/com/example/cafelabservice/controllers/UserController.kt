package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.User
import com.example.cafelabservice.models.dto.SignInRequestDTO
import com.example.cafelabservice.service.OrderService
import com.example.cafelabservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val orderService: OrderService
) {

    @PostMapping("/")
    fun createUser(@RequestBody customer: User): ResponseEntity<Any> {
        return try {
            val data = userService.createUser(customer)
            ResponseEntity.ok(data)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/forgot-password")
    fun requestChangePassword(@RequestBody email: String): ResponseEntity<Any> {
        return try {
            val data = userService.requestPasswordReset(email)
            ResponseEntity.ok(data)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/change-password/{token}")
     fun resetPassword(@PathVariable token: String, @RequestBody password: String): ResponseEntity<Any> {
        return try {
            val data = userService.resetPassword(token, password)
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

    @GetMapping("/")
     fun getAllUsers(): ResponseEntity<Any> {
        return try {
            val users = userService.getAllUsers()
            ResponseEntity.ok(users)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("msg" to e.message))
        }
    }

    @GetMapping("/{userId}/orders")
     fun getOrdersByUserId(@PathVariable userId: String): ResponseEntity<Any> {
        return try {
            val orders = orderService.getOrdersByUserId(userId)
            if (orders.isNotEmpty()) {
                ResponseEntity.ok(orders)
            } else {
                ResponseEntity.status(404).body(mapOf("msg" to "No orders found for this user"))
            }
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("msg" to e.message))
        }
    }
}