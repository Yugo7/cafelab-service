package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.User
import com.example.cafelabservice.models.enums.OrderType
import com.stripe.Stripe
import com.stripe.model.Customer
import com.stripe.model.Invoice
import com.stripe.model.checkout.Session
import com.stripe.param.CustomerCreateParams
import com.stripe.param.CustomerUpdateParams
import com.stripe.param.checkout.SessionCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StripeService {

    @Value("\${stripe.api.key}")
    private lateinit var stripeApiKey: String

    @Value("\${frontend.url}")
    private lateinit var frontendUrl: String

    fun createCheckoutSession(
        productDetails: List<SessionCreateParams.LineItem>,
        order: Order,
        user: User?
    ): Session {
        Stripe.apiKey = stripeApiKey

        // Base session parameters
        val sessionParamsBuilder = SessionCreateParams.builder()
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
            .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
            .setShippingAddressCollection(
                SessionCreateParams.ShippingAddressCollection.builder()
                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.PT)
                    .build()
            )
            .putMetadata("order_id", order.id.toString())
            .setLocale(SessionCreateParams.Locale.PT)

        // Determine session mode
        if (order.type == OrderType.SUBSCRICAO) {
            sessionParamsBuilder.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        } else {
            sessionParamsBuilder
                .setMode(SessionCreateParams.Mode.PAYMENT)
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
        }

        // If user has a Stripe Customer ID, attach it
        user?.stripeId?.let { stripeId ->
              sessionParamsBuilder.setCustomer(stripeId)
//            sessionParamsBuilder.setCustomer("cus_RqDvqjOHXiLAj6")
        }

        val sessionParams = sessionParamsBuilder.build()
        return Session.create(sessionParams)
    }

    fun createStripeCustomer(user: User): Customer {
        Stripe.apiKey = stripeApiKey

        val params = CustomerCreateParams.builder()
            .setEmail(user.email)
            .setName(user.name ?: user.username)
            .build()

        return Customer.create(params)
    }

    fun getInvoice(id: String): Invoice {
        Stripe.apiKey = stripeApiKey
        return Invoice.retrieve(id)
    }

    fun addBalanceToCustomer(customerId: String, amount: Long): Customer {
        Stripe.apiKey = stripeApiKey
        val params = CustomerUpdateParams.builder()
            .setBalance(amount)
            .build()

        return Customer.retrieve(customerId).update(params)
    }

    fun getCustomerBalance(customerId: String): Long {
        Stripe.apiKey = stripeApiKey
        val customer = Customer.retrieve(customerId)
        return customer.balance
    }
}