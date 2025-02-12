package com.example.cafelabservice.service

import com.example.cafelabservice.entity.MailMarketing
import com.example.cafelabservice.repositories.MailMarketingRepository
import org.springframework.stereotype.Service

@Service
class MailMarketingService(
    private val mailMarketingRepo : MailMarketingRepository
) {
    fun signUp(email: String) = mailMarketingRepo.save(MailMarketing(email = email))
}
