package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Analytics
import org.springframework.data.jpa.repository.JpaRepository

interface AnalyticsRepository: JpaRepository<Analytics, Long>