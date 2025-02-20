package com.example.cafelabservice.models.dto

import com.example.cafelabservice.entity.Balance
import com.example.cafelabservice.entity.ChangeType
import jakarta.validation.constraints.NotNull
import java.time.LocalDate


data class BalanceDTO(
    val id: Long?,
    val orderId: Long?,
    val userId: Long?,
    @NotNull(message = "Date cannot be null")
    val date: LocalDate,
    val description: String?,
    @NotNull(message = "Amount cannot be null")
    val amount: Long,
    @NotNull(message = "Type cannot be null")
    val type: ChangeType,
    @NotNull(message = "Category cannot be null")
    val category: String?
){
    companion object {
        fun BalanceDTO.toEntity(): Balance {
            return Balance(
                id = this.id ?: 0,
                order = this.orderId?.let { com.example.cafelabservice.entity.Order(it) },
                user = this.userId?.let { com.example.cafelabservice.entity.User(
                    id = it,
                    email = "placeholder",
                    password = "placeholder"
                ) },
                date = this.date,
                description = this.description,
                value = this.amount,
                changeType = this.type,
                category = this.category
            )
        }
    }
}