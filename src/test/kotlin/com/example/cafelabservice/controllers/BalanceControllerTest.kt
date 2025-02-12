package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.entity.ChangeType
import com.example.cafelabservice.service.BalanceService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime


class BalanceControllerTest(
) {
    private val balanceService: BalanceService = mockk<BalanceService>()
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(BalanceController(balanceService)).build()

    @Test
    fun getAllBalances() {
        val balances = listOf(
            Balance(1, LocalDateTime.now(), LocalDateTime.now(), "Income", "Order completed", 100.0, ChangeType.INCOME),
            Balance(2, LocalDateTime.now(), LocalDateTime.now(), "Expense", "Shipping", -50.0, ChangeType.EXPENSE)
        )
        every { balanceService.getAllBalances() } returns balances

        mockMvc.perform(get("/balances"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
    }

    @Test
    fun getBalanceById() {
    }

    @Test
    fun createBalance() {
    }

    @Test
    fun updateBalance() {
    }

    @Test
    fun deleteBalance() {
    }
}