package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.dto.OrderSummaryDTO
import com.example.cafelabservice.models.enums.OrderStatus
import com.example.cafelabservice.repositories.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.internal.Ref.DoubleRef

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun getAllOrders(pageable: Pageable): Page<Order> = orderRepository.findAll(pageable)

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

    fun getOrderSummary(): OrderSummaryDTO {
        return OrderSummaryDTO(
            orderRepository.countOrdersByType().mapKeys { it.key.name },
            orderRepository.countOrdersByStatus().mapKeys { it.key.nome },
            orderRepository.countOrdersByLastMonths(6)
        )
    }

    fun updateFromCheckoutSessionCompleted(
        orderId: Long,
        receiptUrl: String,
        total: Double,
        userId: String,
        note: String,
        sessionId: String?,
    ) {
        orderRepository.findOrderById(orderId).let { order ->
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
}