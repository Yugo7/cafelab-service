package com.example.cafelabservice.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderSummaryDTO(
    @JsonProperty("ordersByType")
    val countOrdersByType: Map<String, Long>,
    @JsonProperty("ordersByStatus")
    val countOrdersByStatus: Map<String, Long>,
    @JsonProperty("ordersByMonth")
    val ordersByMonth: Map<String, Long>,
)