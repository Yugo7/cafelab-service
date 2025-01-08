package com.example.cafelabservice.service

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.Product
import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CheckoutService() {

    @Value("\${stripe.api.key}")
    private lateinit var stripeApiKey: String
    @Value("\${frontend.url}")
    private lateinit var frontendUrl: String

    private val objectMapper = jacksonObjectMapper().apply {
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    }

    fun generateCheckoutSession(order: Order): Session {
        Stripe.apiKey = stripeApiKey
        val products: List<Product> = objectMapper.readValue(order.products ?: "[]")

        val productDetails = products.map { product ->
            SessionCreateParams.LineItem.builder()
                .setPrice(product.priceId)
                .setQuantity(1L)
                .build()
        }

        val params = SessionCreateParams.builder()
            .setAllowPromotionCodes(true)
            .addAllCustomField(
                listOf(
                    SessionCreateParams.CustomField.builder()
                        .setKey("special_instructions")
                        .setLabel(
                            SessionCreateParams.CustomField.Label.builder()
                                .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
                                .setCustom("Mande-nos uma mensagem")
                                .build()
                        )
                        .setType(SessionCreateParams.CustomField.Type.TEXT)
                        .setOptional(true)
                        .build()
                )
            )
            .setSuccessUrl("$frontendUrl/success")
            .setCancelUrl("$frontendUrl/cancel")
            .addAllLineItem(productDetails)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
            .setShippingAddressCollection(
                SessionCreateParams.ShippingAddressCollection.builder()
                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.PT)
                    .build()
            )
            .putMetadata("order_id", order.id.toString())
            .addAllShippingOption(
                listOf(
                    SessionCreateParams.ShippingOption.builder()
                        .setShippingRateData(
                            SessionCreateParams.ShippingOption.ShippingRateData.builder()
                                .setType(SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT)
                                .setFixedAmount(
                                    SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                                        .setAmount(500L)
                                        .setCurrency("eur")
                                        .build()
                                )
                                .setDisplayName("Envio por CTT")
                                .setDeliveryEstimate(
                                    SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                                        .setMinimum(
                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                                                .setUnit(SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY)
                                                .setValue(5)
                                                .build()
                                        )
                                        .setMaximum(
                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                                                .setUnit(SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY)
                                                .setValue(7)
                                                .build()
                                        )
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
            )
            .setLocale(SessionCreateParams.Locale.PT)
            .setInvoiceCreation(
                SessionCreateParams.InvoiceCreation.builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        val session =  Session.create(params)

        return session
    }
}