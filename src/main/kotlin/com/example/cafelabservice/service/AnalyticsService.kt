package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Analytics
import com.example.cafelabservice.repositories.AnalyticsRepository
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class AnalyticsService(
    private val vercelService: VercelService,
    private val analyticsRepository: AnalyticsRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(AnalyticsService::class.java)
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

    @Scheduled(cron = "1 0 1 * * 1")
    fun fetchAnalyticsDataBean() {
        try {
            vercelService.getTimeSeriesData(
                "event_data",
                LocalDate.parse("2025-03-13").format(DateTimeFormatter.ISO_DATE) + "T00%3A00%3A00.000Z",
                LocalDate.parse("2025-04-12").format(DateTimeFormatter.ISO_DATE) + "T00%3A00%3A00.000Z",
            )?.let { jsonResponse ->
                logger.info("Fetching analytics data: " + jsonResponse.count())
                val data = objectMapper.readValue(jsonResponse, ResponseWithObjects::class.java)
                val analytics = data.items.groupBy { item -> LocalDate.parse(item.key, DateTimeFormatter.ISO_DATE) }
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
        } catch (e: Exception) {
            logger.error("Error fetching analytics data: ${e.message}")
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