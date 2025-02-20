package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.models.dto.BalanceDTO
import com.example.cafelabservice.models.dto.BalanceDTO.Companion.toEntity
import com.example.cafelabservice.models.dto.BalanceDetailsDTO
import com.example.cafelabservice.models.dto.BalanceDetailsDTO.Companion.toDtoList
import com.example.cafelabservice.service.BalanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
    fun getBalancesByTimeSeries(@RequestParam startDate: LocalDate?  = LocalDate.parse("2024-01-01"), @RequestParam endDate: LocalDate? = LocalDate.now()): BalanceDetailsDTO? {
        return balanceService.getBalancesByTimeSeries(startDate!!, endDate!!).ifEmpty { return null }.toDtoList()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBalance(@RequestBody @Valid balance: BalanceDTO): Balance {
        return balanceService.createBalance(balance.toEntity())
    }

    @PutMapping("/{id}")
    fun updateBalance(@PathVariable id: Long, @RequestBody balance: BalanceDTO): ResponseEntity<Balance> {
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