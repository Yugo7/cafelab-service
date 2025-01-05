package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.service.OrderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun getAllOrders() = orderService.getAllOrders()

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long) = orderService.getOrderById(id)

    @PostMapping
    fun createOrder(@RequestBody order: Order) = orderService.createOrder(order)

    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: Long, @RequestBody newOrder: Order) {
        orderService.getOrderById(id).map { existingOrder ->
            val updatedOrder: Order = existingOrder
                .copy(
                    orderPlacedTimestamp = newOrder.orderPlacedTimestamp,
                    orderPaidTimestamp = newOrder.orderPaidTimestamp,
                    status = newOrder.status,
                    products = newOrder.products,
                    totalPaid = newOrder.totalPaid,
                    isSubscription = newOrder.isSubscription
                )
            orderService.updateOrder(existingOrder.id, updatedOrder)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long) = orderService.deleteOrder(id)
}