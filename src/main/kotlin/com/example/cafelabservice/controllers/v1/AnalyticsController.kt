package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.service.VercelService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/analytics")
class AnalyticsController(
    private val vercelService: VercelService
) {
    @GetMapping("/data")
    fun getAnalyticsData(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: LocalDate
    ): Any? {
        return null
    }
}