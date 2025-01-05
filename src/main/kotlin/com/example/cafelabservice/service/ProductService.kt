package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts() = productRepository.findAll()

    fun getProductById(id: Long) = productRepository.findById(id)

    fun createProduct(product: Product) = productRepository.save(product)

    fun updateProduct(id: Long, newProduct: Product) {
        productRepository.findById(id).map { existingProduct ->
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
            productRepository.save(updatedProduct)
        }
    }

    fun deleteProduct(id: Long) = productRepository.deleteById(id)
}