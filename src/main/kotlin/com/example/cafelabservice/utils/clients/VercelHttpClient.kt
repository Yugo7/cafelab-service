package com.example.cafelabservice.utils.clients

import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface VercelHttpClient{

    @GetExchange("/web-analytics/stats")
    fun getWebAnalyticsStats(
        @RequestParam("environment") environment: String,
        @RequestParam("from") from: String,
        @RequestParam("to") to: String,
        @RequestParam("tz") tz: String,
        @RequestParam("type") type: String,
        @RequestParam("teamId") teamId: String,
        @RequestParam("projectId") projectId: String,
        @RequestHeader("Authorization") authorization: String? = "Bearer L3oC8H0BL5N8p2AHUrFdaaxD",
        @RequestHeader("Content-Type") contentType: String? = "application/json"
    ): String

    @GetExchange("/web-analytics/timeseries")
    fun getWebAnalyticsTimeseries(
        @RequestParam("environment") environment: String,
        @RequestParam("from") from: String,
        @RequestParam("to") to: String,
        @RequestParam("tz") tz: String,
        @RequestParam("teamId") teamId: String,
        @RequestParam("projectId") projectId: String,
        @RequestHeader("Authorization") authorization: String? = "Bearer L3oC8H0BL5N8p2AHUrFdaaxD",
        @RequestHeader("Content-Type") contentType: String? = "application/json"
    ): String
}