package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.service.OrderService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.Optional

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val orderService: OrderService = mockk()
    private lateinit var order: Order

    @BeforeEach
    fun setup() {
        order = Order(
            orderPlacedTimestamp = LocalDateTime.now(),
            status = "Processing",
            totalPaid = 100.0,
            isSubscription = false
        )
    }

    @Test
    fun `getAllOrders returns all orders`() {
        every { orderService.getAllOrders() } returns listOf(order)

        mockMvc.perform(get("/orders")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
    }

    @Test
    fun `getOrderById returns order with given id`() {
        every { orderService.getOrderById(order.id) } returns Optional.of(order)

        mockMvc.perform(get("/orders/${order.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
    }

    @Test
    fun `createOrder creates a new order`() {
        every { orderService.createOrder(order) } returns order

        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"orderPlacedTimestamp\":\"${order.orderPlacedTimestamp}\",\"status\":\"${order.status}\",\"totalPaid\":${order.totalPaid},\"isSubscription\":${order.isSubscription}}"))
            .andExpect(status().isOk())
    }

    @Test
    fun `updateOrder updates an existing order`() {
        every { orderService.getOrderById(order.id) } returns Optional.of(order)
        every { orderService.updateOrder(order.id, order) } returns Unit

        mockMvc.perform(put("/orders/${order.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"orderPlacedTimestamp\":\"${order.orderPlacedTimestamp}\",\"status\":\"${order.status}\",\"totalPaid\":${order.totalPaid},\"isSubscription\":${order.isSubscription}}"))
            .andExpect(status().isOk())
    }

    @Test
    fun `deleteOrder deletes an existing order`() {
        every { orderService.getOrderById(order.id) } returns Optional.of(order)
        every { orderService.deleteOrder(order.id) } returns Unit

        mockMvc.perform(delete("/orders/${order.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
    }
}