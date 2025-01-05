package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts() = productService.getAllProducts()

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long) = productService.getProductById(id)

    @PostMapping
    fun createProduct(@RequestBody product: Product) = productService.createProduct(product)

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody newProduct: Product) {
        productService.getProductById(id).map { existingProduct ->
            val updatedProduct: Product = existingProduct
                .copy(
                    name = newProduct.name,
                    description = newProduct.description,
                    brand = newProduct.brand,
                    countryOfOrigin = newProduct.countryOfOrigin,
                    weight = newProduct.weight,
                    price = newProduct.price,
                    cost = newProduct.cost,
                    available = newProduct.available,
                    leastQuantityInStock = newProduct.leastQuantityInStock,
                    firstDateAvailable = newProduct.firstDateAvailable
                )
            productService.updateProduct(existingProduct.id, updatedProduct)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long) = productService.deleteProduct(id)
}