package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.models.dto.EmailRequestIncomingDTO
import com.example.cafelabservice.service.EmailService
import com.example.cafelabservice.service.MailMarketingService
import com.example.cafelabservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/email")
class EmailController(
    private val emailService: EmailService,
    private val mailMarketingService: MailMarketingService,
    private val userService: UserService
) {

    @PostMapping("/template")
    @ResponseStatus(HttpStatus.OK)
    fun sendEmailTemplate(@RequestBody request: EmailRequestIncomingDTO) {
        val to = if(request.broadcast)
        //userService.getAllUsers().map { it.email }
            listOf("japa_yugo@hotmail.com", "yugo_games@hotmail.com")
        else listOfNotNull(request.to)
        require(to.isNotEmpty()) { "No recipients specified" }
        emailService.sendTemplateEmail(to, request.from, request.subject, request.templateName)
    }

    @PostMapping("/content")
    @ResponseStatus(HttpStatus.OK)
    fun sendEmailContent(@RequestBody request: EmailRequestIncomingDTO) {
        val to = if(request.broadcast)
            //userService.getAllUsers().map { it.email }
            listOf("japa_yugo@hotmail.com", "yugo_games@hotmail.com")
        else listOfNotNull(request.to)
        require(to.isNotEmpty()) { "No recipients specified" }
        requireNotNull(request.content) { "No content specified" }
        emailService.sendContentEmail(to, request.from, request.subject, request.content)
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    fun sendEmail() = emailService.sendEmail(null, listOf("japa_yugo@hotmail.com"), "Como está a sua encomenda? ☕", "feedback.html")

    @PostMapping("/mailmarketing/signup")
    @ResponseStatus(HttpStatus.OK)
    fun sendSignUpEmail(@RequestBody email: String) {
        mailMarketingService.signUp(email)
    }
}

