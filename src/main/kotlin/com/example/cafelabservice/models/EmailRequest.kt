package com.example.cafelabservice.models

import com.example.cafelabservice.models.dto.EmailRequestIncomingDTO
import com.example.cafelabservice.service.EmailService

data class EmailRequest(
    val from: String?,
    val to: List<String>,
    val broadcast: Boolean = false,
    val subject: String,
    val templateName: String?,
    val content: String?
) {
    constructor(dto: EmailRequestIncomingDTO) : this(
        from = dto.from,
        to = listOfNotNull(dto.to),
        broadcast = dto.broadcast,
        subject = dto.subject,
        templateName = dto.templateName,
        content = dto.content
    )

    private fun String.wrapMail() = EmailService.wrapHtmlContent(this)
}