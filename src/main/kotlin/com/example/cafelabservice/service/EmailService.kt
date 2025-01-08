package com.example.cafelabservice.service

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {
    fun sendEmail(from: String?, to: String, subject: String, text: String): Boolean {
        return try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)

            helper.setFrom(from ?: "CafeLab PT <atendimento@cafelab.pt>")
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(text, true)

            mailSender.send(message)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}