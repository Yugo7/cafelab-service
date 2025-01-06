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
            productService.updateProduct(existingProduct.id, updatedProduct)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long) = productService.deleteProduct(id)
}