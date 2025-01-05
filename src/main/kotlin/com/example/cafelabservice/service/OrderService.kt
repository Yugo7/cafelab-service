package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.repositories.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun getAllOrders() = orderRepository.findAll()

    fun getOrderById(id: Long) = orderRepository.findById(id)

    fun createOrder(order: Order) = orderRepository.save(order)

    fun updateOrder(id: Long, newOrder: Order) {
        orderRepository.findById(id).map { existingOrder ->
            val updatedOrder: Order = existingOrder
                .copy(
                    orderPlacedTimestamp = newOrder.orderPlacedTimestamp,
                    orderPaidTimestamp = newOrder.orderPaidTimestamp,
                    status = newOrder.status,
                    products = newOrder.products,
                    totalPaid = newOrder.totalPaid,
                    isSubscription = newOrder.isSubscription
                )
            orderRepository.save(updatedOrder)
        }
    }

    fun deleteOrder(id: Long) = orderRepository.deleteById(id)
}