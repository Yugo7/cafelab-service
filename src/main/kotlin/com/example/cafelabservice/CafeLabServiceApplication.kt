package com.example.cafelabservice

import com.example.cafelabservice.config.VercelConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(VercelConfig::class)
class CafeLabServiceApplication

fun main(args: Array<String>) {
    runApplication<CafeLabServiceApplication>(*args)
}
