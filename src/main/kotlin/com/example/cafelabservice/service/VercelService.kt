package com.example.cafelabservice.service

import com.example.cafelabservice.config.VercelConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URI

@Service
class VercelService(
    private val vercel: VercelConfig,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(VercelService::class.java)

    fun getStatsData(type: String): String? {
        val uri = StringBuilder(vercel.url)
            .append("stats")
            .append("?environment=production")
            .append("&from=2025-01-07T00%3A00%3A00.000Z")
            .append("&projectId=").append(vercel.projectId)
            .append("&teamId=").append(vercel.teamId)
            .append("&to=2025-01-14T00%3A00%3A00.000Z")
            .append("&tz=America%2FNew_York")
            .append("&type=").append(type)
            .toString()
        val url = URI(uri).toURL()

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer ${vercel.token}")
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.inputStream.bufferedReader().use { it.readText() }.also {
                logger.info("GET request successful, message: $it")
            }
        } else {
            val response = connection.errorStream.bufferedReader().use { it.readText() }
            val jsonResponse = objectMapper.readTree(response)
            logger.error("GET request failed: $responseCode, message: ${jsonResponse["error"]["message"]}")
            return null
        }
    }

    fun getTimeSeriesData(type: String, from: String, to: String): String? {
        val uri = StringBuilder(vercel.url)
            .append("timeseries")
            .append("?environment=production")
            .append("&from=").append(from)
            .append("&projectId=").append(vercel.projectId)
            .append("&teamId=").append(vercel.teamId)
            .append("&to=").append(to)
            .append("&tz=Europe%2FLisbon")
            .toString()
        val url = URI(uri).toURL()

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer ${vercel.token}")
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.inputStream.bufferedReader().use { it.readText() }.also {
                logger.info("GET request successful")
            }
        } else {
            val response = connection.errorStream.bufferedReader().use { it.readText() }
            val jsonResponse = objectMapper.readTree(response)
            logger.error("GET request failed: $responseCode, message: ${jsonResponse["error"]["message"]}")
            return null
        }
    }
}