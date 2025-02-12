package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Analytics
import com.example.cafelabservice.repositories.AnalyticsRepository
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class AnalyticsService(
    private val vercelService: VercelService,
    private val analyticsRepository: AnalyticsRepository
) {
    private val objectMapper = jacksonObjectMapper()

    @Scheduled(cron = "0 0 1 * * *")
    fun fetchAnalyticsData() {
        vercelService.getTimeSeriesData(
            "event_data",
            LocalDate.now().minusDays(1).toString() + "T00%3A00%3A00.000Z",
            LocalDate.now().toString() + "T00%3A00%3A00.000Z"
        )?.let { jsonResponse ->
            val data = objectMapper.readValue(jsonResponse, ResponseWithObjects::class.java)
            val analytics = data.items.groupBy { item -> LocalDate.parse(item.key, DateTimeFormatter.ISO_DATE_TIME) }
                .map { (date, items) ->
                    Analytics(
                        date = date,
                        accesses = items.sumOf { it.total.toLong() },
                        visitors = items.sumOf { it.devices.toLong() },
                        bounceRate = items.sumOf { it.bounceRate } / items.size
                    )
                }
            analyticsRepository.saveAll(analytics)
        }
    }
}

data class ResponseWithObjects(
    @JsonProperty("data")
    val items: List<DataEntry>,
)

data class DataEntry(
    val key: String,
    val total: Int,
    val devices: Int,
    val bounceRate: Int
)