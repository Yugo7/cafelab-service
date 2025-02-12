package com.example.cafelabservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vercel")
data class VercelConfig(
    val token: String,
    val projectId: String,
    val teamId: String,
    val url: String
)