package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.PasswordToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PasswordTokenRepository : JpaRepository<PasswordToken, Long> {
}