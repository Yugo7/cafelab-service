package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.repositories.ProductRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProductServiceTest {

    private val productRepository = mockk<ProductRepository>()
    private val productService = ProductService(productRepository)

    @Test
    fun `getProductById returns product with given id`() {
        val product = Product(
            name = "Test Product",
            description = "This is a test product",
            brand = "Test Brand",
            countryOfOrigin = "Test Country",
            weight = 1.0,
            price = 10.0,
            cost = 5.0,
            available = true,
            leastQuantityInStock = 10,
            firstDateAvailable = LocalDateTime.now()
        )

        every { productRepository.findById(1L) } returns Optional.of(product)

        val foundProduct = productService.getProductById(1L)

        assertEquals(product, foundProduct.get())
    }
}