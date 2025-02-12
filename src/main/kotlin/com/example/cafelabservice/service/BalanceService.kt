package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.repositories.BalanceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

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

    fun updateBalance(id: Long, balance: Balance): Balance? {
        return if (balanceRepository.existsById(id)) {
            balanceRepository.save(balance.copy(id = id))
        } else {
            null
        }
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