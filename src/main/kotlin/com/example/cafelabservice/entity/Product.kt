package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "products")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val nomePt: String,
    val descricaoPt: String = "",
    val origem: String = "",
    val grao: String = "",
    val preco: Double = 0.0,
    val imagem: String? = null,
    @Enumerated(EnumType.STRING)
    val secao: Secao = Secao.BOUTIQUE,
    val descricaoEn: String? = null,
    val nomeEn: String? = null,
    val priceId: String? = null,
    val sizePt: String? = null,
    val sizeEn: String? = null,
    val isActive: Boolean = false
)

enum class Secao {
    BOUTIQUE, // Add other sections if needed
}