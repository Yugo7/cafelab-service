package com.example.cafelabservice.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SignInRequestDTO(
    @JsonProperty("email")
    var email: String,
    @JsonProperty("password")
    var password: String
)
