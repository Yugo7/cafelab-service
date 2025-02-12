package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.MailMarketing
import org.springframework.data.jpa.repository.JpaRepository

interface MailMarketingRepository: JpaRepository<MailMarketing, Long> {
    fun findByEmail(email: String): MailMarketing?
}
