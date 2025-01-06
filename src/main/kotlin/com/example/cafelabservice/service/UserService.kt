package com.example.cafelabservice.service

import com.example.cafelabservice.entity.User
import com.example.cafelabservice.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers() = userRepository.findAll()

    fun getUserById(id: Long) = userRepository.findById(id)

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
}