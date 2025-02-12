package com.example.cafelabservice.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderRequestDTO(
    val cart: CartDTO
)

data class CartDTO(
    val user: UserDTO?,
    val items: List<ItemDTO>,
    @JsonProperty("variety")
    val grindSize: String
)

data class ItemDTO(
    val id: Long,
    val quantity: Int
)