package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.OrderSubscription
import com.example.cafelabservice.models.dto.OrderSummaryDTO
import com.example.cafelabservice.models.enums.OrderStatus
import com.example.cafelabservice.repositories.OrderRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productService: ProductService,
    private val subscriptionService: SubscriptionService
) {
    private val logger: Logger = LoggerFactory.getLogger(AnalyticsService::class.java)
    fun getAllOrders(pageable: Pageable): Page<Order> = orderRepository.findAllActiveOrders(pageable)

    fun getOrderById(id: Long): Order = orderRepository.findById(id).orElseThrow { throw NoSuchElementException() }

    fun createOrder(order: Order) = orderRepository.save(order)

    fun updateOrder(id: Long, newOrder: Order) {
        orderRepository.findById(id).map { existingOrder ->
            val updatedOrder: Order = existingOrder
                .copy(
                    orderProducts = newOrder.orderProducts,
                    receiptUrl = newOrder.receiptUrl,
                    total = newOrder.total,
                    status = newOrder.status,
                    createdAt = newOrder.createdAt
                )
            orderRepository.save(updatedOrder)
        }
    }

    fun deleteOrder(id: Long) = orderRepository.deleteById(id)

    fun getOrdersByUserId(userId: String) = orderRepository.findAllByUserId(userId)
    fun getOrdersByUser(user: Long) = orderRepository.findAllLojaOrdersByUser(user)

    fun getSubscriptionsByUser(user: Long): List<OrderSubscription>? {
        val orders = orderRepository.findAllSubscriptionsByUser(user).ifEmpty { return null }
        return orders.mapNotNull { order ->
            val subscription = subscriptionService.getSubscriptionById(order.orderProducts.firstOrNull()?.productId ?: return@mapNotNull null).orElse(null)
            if (subscription == null) {
                logger.error("Subscription not found for order: $order")
                null
            } else {
                OrderSubscription(order, subscription)
            }
        }
    }

    fun getOrderSummary(): OrderSummaryDTO {
        return OrderSummaryDTO(
            orderRepository.countOrdersByType().mapKeys { it.key.name },
            orderRepository.countOrdersByStatus().mapKeys { it.key.nome },
            orderRepository.countOrdersByLastMonths(6)
        )
    }

    fun updateFromCheckoutSessionCompleted(
        orderId: Long,
        receiptUrl: String?,
        total: String,
        userId: String,
        note: String,
        sessionId: String?,
    ): Order? = orderRepository.findOrderById(orderId).let { order ->
            orderRepository.save(
                order.copy(
                    receiptUrl = receiptUrl,
                    status = OrderStatus.PAYMENT_SUCCESSFUL,
                    total = total,
                    userId = userId,
                    note = note,
                    sessionId = sessionId
                )
            )
        }
    }