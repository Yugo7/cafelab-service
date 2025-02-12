package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface BalanceRepository : JpaRepository<Balance, Long>{
    fun findAllByDateBetween(startDate: LocalDate, endDate: LocalDate): List<Balance>
}