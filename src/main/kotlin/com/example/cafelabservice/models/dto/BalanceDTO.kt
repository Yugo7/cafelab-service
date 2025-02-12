package com.example.cafelabservice.models.dto

import com.example.cafelabservice.entity.ChangeType
import java.time.LocalDate

data class BalanceDTO(
    val id: Long,
    val orderId: Long?,
    val userId: Long?,
    val date: LocalDate,
    val description: String?,
    val amount: Long,
    val type: ChangeType,
    val category: String?
)