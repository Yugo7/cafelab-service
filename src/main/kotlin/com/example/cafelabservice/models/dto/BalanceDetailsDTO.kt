package com.example.cafelabservice.models.dto

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.entity.ChangeType
import java.time.LocalDate

data class BalanceDetailsDTO(
    val balance: Long,
    val income: Long,
    val expenses: Long,
    val details: List<BalanceDTO>,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    companion object {
        fun List<Balance>.toDtoList(): BalanceDetailsDTO {
            val balanceList = mutableListOf<BalanceDTO>()
            var totalIncome = 0L
            var totalExpenses = 0L

            this.forEach { balance ->
                val balanceDTO = BalanceDTO(
                    id = balance.id,
                    orderId = balance.order?.id,
                    userId = balance.user?.id,
                    date = balance.date,
                    description = balance.description,
                    amount = balance.value,
                    type = balance.changeType,
                    category = balance.category
                )
                balanceList.add(balanceDTO)
                if (balanceDTO.type == ChangeType.INCOME) {
                    totalIncome += balanceDTO.amount
                } else if (balanceDTO.type == ChangeType.EXPENSE) {
                    totalExpenses += balanceDTO.amount
                }
            }

            return BalanceDetailsDTO(
                balance = totalIncome - totalExpenses,
                income = totalIncome,
                expenses = totalExpenses,
                details = balanceList,
                startDate = balanceList.minOf { it.date },
                endDate = balanceList.maxOf { it.date }
            )
        }
    }

}
