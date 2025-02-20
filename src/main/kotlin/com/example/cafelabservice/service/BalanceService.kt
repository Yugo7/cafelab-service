package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.User
import com.example.cafelabservice.exception.ResourceNotFoundException
import com.example.cafelabservice.models.dto.BalanceDTO
import com.example.cafelabservice.repositories.BalanceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class BalanceService(private val balanceRepository: BalanceRepository) {

    fun getAllBalances(): List<Balance> {
        return balanceRepository.findAll()
    }

    fun getBalanceById(id: Long): Balance? {
        return balanceRepository.findById(id).orElse(null)
    }

    fun createBalance(balance: Balance): Balance {
        return balanceRepository.save(balance)
    }

    fun getBalancesByTimeSeries(startDate: LocalDate, endDate: LocalDate): List<Balance> {
        return balanceRepository.findAllByDateBetween(startDate, endDate)
    }

    fun updateBalance(id: Long, balance: BalanceDTO): Balance? {
        val existingBalance = balanceRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException("Balance id: $id not found")
        val updatedBalance = existingBalance.copy(
            order = balance.orderId?.let { Order(it) },
            user = balance.userId?.let { User(
                id = it,
                email = "placeholder",
                password = "placeholder"
            ) },
            date = balance.date,
            description = balance.description,
            value = balance.amount,
            changeType = balance.type,
            category = balance.category,
            updatedAt = LocalDateTime.now()
        )
        return balanceRepository.save(updatedBalance)
    }

    fun deleteBalance(id: Long): Boolean {
        return if (balanceRepository.existsById(id)) {
            balanceRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}