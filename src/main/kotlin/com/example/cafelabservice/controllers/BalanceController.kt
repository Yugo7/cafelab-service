package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.models.dto.BalanceDetailsDTO
import com.example.cafelabservice.models.dto.BalanceDetailsDTO.Companion.toDtoList
import com.example.cafelabservice.service.BalanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/balances")
class BalanceController(private val balanceService: BalanceService) {

    @GetMapping
    fun getAllBalances(): List<Balance> {
        return balanceService.getAllBalances()
    }

    @GetMapping("/{id}")
    fun getBalanceById(@PathVariable id: Long): ResponseEntity<Balance> {
        val balance = balanceService.getBalanceById(id)
        return if (balance != null) {
            ResponseEntity.ok(balance)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/timeseries")
    fun getBalancesByTimeSeries(@RequestParam startDate: LocalDate?  = LocalDate.parse("2024-01-01"), @RequestParam endDate: LocalDate? = LocalDate.now()): BalanceDetailsDTO {
        return balanceService.getBalancesByTimeSeries(startDate!!, endDate!!).toDtoList()
    }

    @PostMapping
    fun createBalance(@RequestBody balance: Balance): Balance {
        return balanceService.createBalance(balance)
    }

    @PutMapping("/{id}")
    fun updateBalance(@PathVariable id: Long, @RequestBody balance: Balance): ResponseEntity<Balance> {
        val updatedBalance = balanceService.updateBalance(id, balance)
        return if (updatedBalance != null) {
            ResponseEntity.ok(updatedBalance)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteBalance(@PathVariable id: Long): ResponseEntity<Void> {
        return if (balanceService.deleteBalance(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}