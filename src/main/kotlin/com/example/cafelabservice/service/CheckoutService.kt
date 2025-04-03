package com.example.cafelabservice.service

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.OrderProduct
import com.example.cafelabservice.models.OrderToCreate
import com.example.cafelabservice.models.dto.CartDTO
import com.example.cafelabservice.models.dto.OrderRequestDTO
import com.example.cafelabservice.models.dto.checkout.SubscriptionRequestDTO
import com.example.cafelabservice.models.enums.OrderStatus
import com.example.cafelabservice.models.enums.OrderType
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import org.springframework.stereotype.Service

@Service
class CheckoutService(
    private val subscriptionService: SubscriptionService,
    private val stripeService: StripeService,
    private val orderService: OrderService,
    private val productService: ProductService,
    private val userService: UserService
) {
    private val objectMapper = jacksonObjectMapper().apply {
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    }

    fun generateCheckoutSession(orderDTO: OrderRequestDTO): Session {
        val toCreate = createOrderFromCart(orderDTO.cart)

        val order = orderService.createOrder(
            toCreate.toOrder()
        )

        val productDetails = toCreate.orderProducts.map { (product, quantity) ->
            SessionCreateParams.LineItem.builder()
                //.setPrice(product.priceId)
                .setPrice("price_1QodX7RqqMn2mwDSf7dfh0Ig")
                .setQuantity(quantity.toLong())
                .build()
        }

        val user = orderDTO.cart.user?.email?.let { userService.getUserByUsername(it) }

        return stripeService.createCheckoutSession(
            productDetails,
            order,
            user
        )
    }

    fun generateCheckoutSessionForSubscriptions(subscriptionRequestDTO: SubscriptionRequestDTO): Session {
        val subscription = subscriptionService.getSubscriptionById(subscriptionRequestDTO.subscription.id).orElseThrow { throw Exception("Subscription not found") }

        val order = orderService.createOrder(
            Order(
                orderProducts = emptyList(),
                total = subscription.price,
                status = OrderStatus.PENDING,
                variety = subscriptionRequestDTO.subscription.grindSize,
                type = OrderType.SUBSCRICAO,
                isTest = true
            )
        )

        val user = subscriptionRequestDTO.user?.email?.let { userService.getUserByUsername(it) }

        val productDetails = SessionCreateParams.LineItem.builder()
            //.setPrice(subscription.priceId)
            .setPrice("price_1QodX7RqqMn2mwDSf7dfh0Ig")
            .setQuantity(1L)
            .build()

        return stripeService.createCheckoutSession(
            listOf(productDetails),
            order,
            user
        )
    }

    fun createOrderFromCart(cart: CartDTO): OrderToCreate{
        val productQuantityMap = cart.items.associate {
            val product = productService.getProductById(it.id).orElseThrow { Exception("Product id " + it.id + " not found") }
            product to it.quantity
        }
        val total = productQuantityMap.entries.sumOf { (product, quantity) -> product.preco.toBigInteger().times(quantity.toBigInteger()) }

        return OrderToCreate(
            orderProducts = productQuantityMap,
            total = total.toString(),
            status = OrderStatus.CREATED,
            variety = cart.grindSize,
            type = OrderType.LOJA,
            isTest = true,
            objectMapper = objectMapper
        )
    }
}
