package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.models.OrderProduct
import com.example.cafelabservice.repositories.ProductRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    //@PreAuthorize("hasRole('admin')")
    fun getAllProducts() = productRepository.findAll()

    fun getActiveProducts() = productRepository.findAllByIsActiveTrue()

    fun getProductById(id: Long) = productRepository.findById(id)

    fun createProduct(product: Product) = productRepository.save(product)

    fun updateProduct(id: Long, newProduct: Product) {
        productRepository.findById(id).map { existingProduct ->
            val updatedProduct: Product = existingProduct
                .copy(
                    nomePt = newProduct.nomePt,
                    descricaoPt = newProduct.descricaoPt,
                    origem = newProduct.origem,
                    grao = newProduct.grao,
                    preco = newProduct.preco,
                    imagem = newProduct.imagem,
                    secao = newProduct.secao,
                    descricaoEn = newProduct.descricaoEn,
                    nomeEn = newProduct.nomeEn,
                    priceId = newProduct.priceId,
                    sizePt = newProduct.sizePt,
                    sizeEn = newProduct.sizeEn,
                    isActive = newProduct.isActive
                )
            productRepository.save(updatedProduct)
        }
    }

    fun deleteProduct(id: Long) = productRepository.deleteById(id)

    fun toProduct(orderProduct: OrderProduct): Product? {
        return productRepository.findById(orderProduct.productId).orElse(null)
    }
}