package com.example.cafelabservice.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class EmailRequestIncomingDTO(
    val from: String?,
    @JsonProperty("recipientEmail")
    val to: String?,
    @JsonProperty("sendToAll")
    val broadcast: Boolean = false,
    @JsonProperty("title")
    val subject: String,
    val templateName: String?,
    val content: String?,
)