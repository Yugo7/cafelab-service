package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.EmailLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailLogRepository : JpaRepository<EmailLog, Long>