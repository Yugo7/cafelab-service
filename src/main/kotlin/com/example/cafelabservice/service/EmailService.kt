package com.example.cafelabservice.service

import com.example.cafelabservice.entity.EmailLog
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.repositories.EmailLogRepository
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val emailLogRepository: EmailLogRepository,
    private val productService: ProductService
) {

    @Value("\${frontend.url}")
    private lateinit var frontendUrl: String

    fun sendEmail(from: String?, to: List<String>, subject: String, emailContent: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper =
            MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name())

        helper.setFrom(from ?: "CafeLab PT<atendimento@cafelab.pt>")
        helper.setTo(to.toTypedArray())
        helper.setSubject(subject)

        helper.setText(emailContent, true)

        mailSender.send(message)

        val emailLog = EmailLog(
            from = from ?: "CafeLab PT<atendimento@cafelab.pt>",
            to = to.joinToString(","),
            subject = subject,
            content = emailContent
        )
        emailLogRepository.save(emailLog)
    }

    fun sendOrderConfirmationEmail(to: String, order: Order) {
        val orderConfirmationTemplate = ClassPathResource("templates/order-confirmation.html")
        val productItemTemplate = ClassPathResource("templates/components/product-item.html")

        val orderConfirmationContent = Files.readString(orderConfirmationTemplate.file.toPath(), StandardCharsets.UTF_8)

        val productsHtml = order.orderProducts.joinToString(separator = "") { orderProduct ->
            val product = productService.getProductById(orderProduct.productId).orElseThrow { throw NoSuchElementException("No product with id ${orderProduct.productId}") }
            Files.readString(productItemTemplate.file.toPath(), StandardCharsets.UTF_8)
                    .replace("{{quantity}}", orderProduct.quantity.toString())
                    .replace("{{product_name}}", product.nomePt ?: product.nomeEn ?: "")
                    .replace("{{description_1}}", product.descricaoPt)
                    .replace("{{description_2}}", product.origem)
                    .replace("{{price}}", product.preco.toString())
        }

        val finalEmailContent = orderConfirmationContent.replace("{{products_list}}", productsHtml)

        sendEmail(null, listOf(to), "Order Confirmation #${order.id}", finalEmailContent)
    }

    fun sendPasswordResetEmail(to: String, token: String) {
        val resource = ClassPathResource("templates/password-change.html")
        val emailContent = Files.readString(resource.file.toPath(), StandardCharsets.UTF_8)
        val resetLink = "$frontendUrl/reset-password/$token"

        sendEmail(null, listOf(to), "Recuperação de senha", emailContent.replace("{{resetLink}}", resetLink))
    }

    fun sendContentEmail(to: List<String>, from: String?, subject: String, content: String) {
        require(content.isNotBlank()) { "Email content cannot be empty" }
        val emailContent = wrapHtmlContent(content)
        sendEmail(from, to, subject, emailContent)
    }

    fun sendTemplateEmail(to: List<String>, from: String?, subject: String, templateName: String?) {
        require(!templateName.isNullOrBlank()) { "Email template cannot be empty" }

        val resource = ClassPathResource("templates/$templateName.html")
        val emailContent = Files.readString(resource.file.toPath(), StandardCharsets.UTF_8)
        sendEmail(from, to, subject, emailContent)
    }

    companion object {
        fun wrapHtmlContent(content: String): String {
            val resource = ClassPathResource("templates/empty-template.html")
            val wrapper = Files.readString(resource.file.toPath(), StandardCharsets.UTF_8)
            return wrapper.replace("{{custom-content}}", content)
        }
    }
}

