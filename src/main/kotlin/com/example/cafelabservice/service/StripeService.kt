package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.enums.OrderType
import com.stripe.Stripe
import com.stripe.model.Invoice
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StripeService {

    @Value("\${stripe.api.key}")
    private lateinit var stripeApiKey: String

    @Value("\${frontend.url}")
    private lateinit var frontendUrl: String

    fun createCheckoutSession(productDetails: List<SessionCreateParams.LineItem>, order: Order) : Session {
        Stripe.apiKey = stripeApiKey

        var sessionParams = SessionCreateParams.builder()
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
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
            .setShippingAddressCollection(
                SessionCreateParams.ShippingAddressCollection.builder()
                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.PT)
                    .build()
            )
            .putMetadata("order_id", "1")
            .setLocale(SessionCreateParams.Locale.PT)
            .build()

        if (order.type != OrderType.SUBSCRICAO) {
            val newSessionParamsBuilder = SessionCreateParams.builder()
                .setAllowPromotionCodes(sessionParams.allowPromotionCodes)
                .addAllCustomField(sessionParams.customFields)
                .setSuccessUrl(sessionParams.successUrl)
                .setCancelUrl(sessionParams.cancelUrl)
                .addAllLineItem(sessionParams.lineItems)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setBillingAddressCollection(sessionParams.billingAddressCollection)
                .setShippingAddressCollection(sessionParams.shippingAddressCollection)
                .putMetadata("order_id", "1")
                .setLocale(sessionParams.locale)
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
                .setInvoiceCreation(
                    SessionCreateParams.InvoiceCreation.builder()
                        .setEnabled(true)
                        .build()
                )

            sessionParams = newSessionParamsBuilder.build()
        }

        val session = Session.create(sessionParams)

        return session
    }

    fun getInvoice(id: String): Invoice {
        Stripe.apiKey = stripeApiKey
        return Invoice.retrieve(id)
    }
}