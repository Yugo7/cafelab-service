package com.example.cafelabservice.entity

import com.example.cafelabservice.models.OrderProduct
import com.example.cafelabservice.models.dto.OrderResponseDTO
import com.example.cafelabservice.models.dto.ProductViewDTO
import com.example.cafelabservice.models.enums.OrderStatus
import com.example.cafelabservice.models.enums.OrderType
import com.example.cafelabservice.service.ProductService
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.time.ZonedDateTime

@Entity
@Table(name = "\"order\"")
@Convert(attributeName = "pgsql_enum", converter = PostgreSQLEnumJdbcType::class)
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ElementCollection
    @CollectionTable(name = "order_product", indexes = [Index(name = "id", columnList = "order_Id")])
    val orderProducts: List<OrderProduct> = mutableListOf(),

    val total: Double? = null,
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    val status: OrderStatus = OrderStatus.PENDING,
    @Column(name = "user_id")
    val userId: String? = null,
    @Column(name = "\"user\"")
    val user: Int? = null,
    val userStripeId: String? = null,
    val sessionId: String? = null,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val receiptUrl: String? = null,
    val variety: String? = null,
    val note: String? = null,
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    val type: OrderType = OrderType.LOJA,
    val isTest: Boolean = false
) {
    fun toOrderResponseDTO(productService: ProductService): OrderResponseDTO {
        return OrderResponseDTO(
            id = id,
            products = orderProducts.map { ProductViewDTO.fromProduct(productService.getProductById(it.productId).orElseThrow { Exception("Product not found") }) },
            status = status.nome,
            user = user.toString(),
            createdAt = createdAt,
            receiptUrl = receiptUrl,
            grindSize = variety ?: "",
            note = note,
            type = type
        )
    }
}
