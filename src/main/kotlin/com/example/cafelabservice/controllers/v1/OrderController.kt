package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.dto.OrderResponseDTO
import com.example.cafelabservice.models.dto.OrderSummaryDTO
import com.example.cafelabservice.models.dto.toOrderResponseDTO
import com.example.cafelabservice.service.OrderService
import com.example.cafelabservice.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
    private val productService: ProductService
) {

    @GetMapping
    fun getAllOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<OrderResponseDTO> {
        val pageable = PageRequest.of(page, size)
        return orderService.getAllOrders(pageable).map { order -> order.toOrderResponseDTO(productService) }
    }
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long) = orderService.getOrderById(id)

    @GetMapping("/summary")
    fun getOrderSummary(): OrderSummaryDTO {
        return orderService.getOrderSummary()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createOrder(@RequestBody order: Order) = orderService.createOrder(order)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateOrder(@PathVariable id: Long, @RequestBody newOrder: Order) {
        val existingOrder = orderService.getOrderById(id)
        val updatedOrder: Order = existingOrder.copy(
            status = newOrder.status,
            orderProducts = newOrder.orderProducts
        )
        orderService.updateOrder(existingOrder.id, updatedOrder)
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long) = orderService.deleteOrder(id)
}