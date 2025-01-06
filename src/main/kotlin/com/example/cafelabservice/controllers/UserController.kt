package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.User
import com.example.cafelabservice.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers() = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long) = userService.getUserById(id)

    @PostMapping
    fun createUser(@RequestBody user: User) = userService.createUser(user)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody newUser: User) {
        userService.getUserById(id).map { existingUser ->
            val updatedUser: User = existingUser
                .copy(
                    name = newUser.name,
                    email = newUser.email,
                    password = newUser.password,
                    role = newUser.role
                )
            userService.updateUser(existingUser.id, updatedUser)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) = userService.deleteUser(id)
}