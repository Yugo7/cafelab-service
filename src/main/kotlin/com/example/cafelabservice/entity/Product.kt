package com.example.cafelabservice.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity
@Table(name = "products")
@Convert(attributeName = "pgsql_enum", converter = PostgreSQLEnumJdbcType::class)
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "created_at")
    @JsonProperty("created_at")
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    @Column(name = "nome_pt")
    @JsonProperty("nome_pt")
    val nomePt: String?,
    @Column(name = "descricao_pt")
    @JsonProperty("descricao_pt")
    val descricaoPt: String = "",
    val origem: String = "",
    val grao: String = "",
    val preco: Double = 0.0,
    val imagem: String? = null,
    @Enumerated(EnumType.STRING)
    val secao: Secao = Secao.BOUTIQUE,
    @Column(name = "descricao_en")
    @JsonProperty("descricao_en")
    val descricaoEn: String? = null,
    @Column(name = "nome_en")
    @JsonProperty("nome_en")
    val nomeEn: String? = null,
    @Column(name = "price_id")
    @JsonProperty("price_id")
    val priceId: String? = null,
    @Column(name = "size_pt")
    @JsonProperty("size_pt")
    val sizePt: String? = null,
    @Column(name = "size_en")
    @JsonProperty("size_en")
    val sizeEn: String? = null,
    @Column(name = "is_active")
    @JsonProperty("is_active")
    val isActive: Boolean = false
)

enum class Secao {
    BOUTIQUE,CAFE,VOUCHER
}