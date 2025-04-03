package com.example.cafelabservice.service

import com.example.cafelabservice.entity.EmailLog
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.models.ShippingInfo
import com.example.cafelabservice.repositories.EmailLogRepository
import com.example.cafelabservice.utils.MoneyFormatter.Companion.formatToEuros
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val emailLogRepository: EmailLogRepository,
    private val productService: ProductService,
    private val templateEngine: TemplateEngine
) {

    @Value("\${frontend.url}")
    private lateinit var frontendUrl: String

    fun sendEmail(from: String?, to: List<String>, subject: String, emailContent: String, attachments: List<Pair<String, ByteArray>>? = null) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper =
            MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name())

        helper.setFrom(from ?: "CafeLab PT<atendimento@cafelab.pt>")
        helper.setTo(to.toTypedArray())
        helper.setSubject(subject)

        attachments?.forEach {
            helper.addAttachment(it.first, ByteArrayResource(it.second))
        }

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

    fun sendOrderConfirmationEmail(to: String, order: Order, shipping: ShippingInfo?, pdfData: ByteArray?) {
        val formattedOrder = order.copy(total = order.total?.let { formatToEuros(it) })
        val variables = mutableMapOf<String, Any>("order" to formattedOrder)

        val attachments = pdfData?.let { mutableListOf(Pair("Recibo pedido #${order.id}.pdf", pdfData)) }

        buildProductSummaries(order).run { variables["products"] = this }

        shipping?.let { ship ->
            val formattedShippingCost = ship.shippingCost?.toLongOrNull()?.let { amount ->
                if (amount > 0) formatToEuros(ship.shippingCost) else "Grátis"
            }
            variables["shipping"] = ship.copy(shippingCost = formattedShippingCost, address = ship.getShippingAddressString())
        }
        val finalEmailContent = generateEmailContent("order-confirmation", variables)
        sendEmail(null, listOf(to), "Order Confirmation #${order.id}", finalEmailContent, attachments)
    }

    fun sendGiftCardEmail(to: String, giftCardCode: String, amount: Long) {
        val variables = mapOf(
            "giftCardCode" to giftCardCode,
            "amount" to amount
        )
        val emailContent = generateEmailContent("gift-card", variables)
        sendEmail(null, listOf(to), "Your Gift Card", emailContent)
    }

    fun buildProductSummaries(order: Order): List<ProductSummary> {
        return order.orderProducts.map { op ->
            val product = productService.getProductById(op.productId).orElseThrow()
            val productCopy = product.copy(preco = formatToEuros(product.preco))
            ProductSummary(
                productCopy,
                op.quantity,
                formatToEuros(product.preco.toLong().times(op.quantity.toLong()).toString())
            )
        }
    }

    fun generateEmailContent(templateName: String, variables: Map<String, Any>): String {
        val context = Context()
        context.setVariables(variables)
        return templateEngine.process(templateName, context)
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

data class ProductSummary(
    val product: Product,
    val quantity: Int,
    val total: String
)