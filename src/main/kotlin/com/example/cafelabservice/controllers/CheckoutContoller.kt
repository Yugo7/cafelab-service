package com.example.cafelabservice.controllers

import com.example.cafelabservice.service.CheckoutService
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.OrderType
import com.example.cafelabservice.entity.dto.SessionDTO
import com.example.cafelabservice.service.EmailService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checkout")
class CheckoutContoller(
    private val checkoutService: CheckoutService,
    private val objectMapper: ObjectMapper,
    private val emailService: EmailService
) {

    val mockOrder = Order(
        id = 1,
        products = """
        [
            {
                "id": 1,
                "nomePt": "Café Expresso",
                "descricaoPt": "Café expresso de alta qualidade",
                "origem": "Brasil",
                "grao": "Arábica",
                "preco": 2.5,
                "priceId": "price_1PB56VRqqMn2mwDSrKBjeNCk",
                "secao": "BOUTIQUE",
                "isActive": true
            },
            {
                "id": 2,
                "nomePt": "Café Latte",
                "descricaoPt": "Café latte com leite vaporizado",
                "origem": "Colômbia",
                "grao": "Robusta",
                "priceId": "price_1PB56VRqqMn2mwDSrKBjeNCk",
                "preco": 3.0,
                "secao": "BOUTIQUE",
                "isActive": true
            }
        ]
    """.trimIndent(),
        total = 5.5,
        status = "PENDING",
        userId = "user123",
        user = """
        {
            "name": "John Doe",
            "email": "john.doe@example.com"
        }
    """.trimIndent(),
        userStripeId = "stripe_user_123",
        sessionId = "session_123",
        receiptUrl = "http://example.com/receipt",
        variety = "Variety",
        note = "Please deliver between 9 AM and 5 PM",
        type = OrderType.LOJA,
        isTest = true
    )

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun checkout() = SessionDTO.fromSession(checkoutService.generateCheckoutSession(mockOrder))

    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    fun sendEmail() = emailService.sendEmail(from = null, "japa_yugo@hotmail.com", "teste", "Teste de email")

}