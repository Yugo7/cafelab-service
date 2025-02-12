package com.example.cafelabservice.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "balance")
@Convert(attributeName = "pgsql_enum", converter = PostgreSQLEnumJdbcType::class)
data class Balance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val date: LocalDate = LocalDate.now(),
    val name: String? = null,
    val description: String? = null,
    val value: Long = 0,
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    val changeType: ChangeType,
    val category: String? = null,
    @ManyToOne
    @JoinColumn(name = "order_id")
    val order: Order? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null
)

enum class ChangeType {
    INCOME, EXPENSE
}