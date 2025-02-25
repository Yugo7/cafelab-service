package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.enums.OrderStatus
import com.example.cafelabservice.models.enums.OrderStatus.Companion.validStatuses
import com.example.cafelabservice.models.enums.OrderType
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime

interface OrderRepository : JpaRepository<Order, Long>, OrderRepositoryCustom {
    fun findAllByUserId(userId: String): List<Order>
    fun findOrderById(id: Long): Order
    override fun findAll(pageable: Pageable): Page<Order>
}

interface OrderRepositoryCustom {
    fun countOrdersByType(): Map<OrderType, Long>

    fun countOrdersByStatus(): Map<OrderStatus, Long>

    fun countOrdersByLastMonths(numberMonths: Int): Map<String, Long>

    fun findAllActiveOrders(pageable: Pageable): Page<Order>
}

class OrderRepositoryCustomImpl(
    private val entityManager: EntityManager
) : OrderRepositoryCustom {

    override fun countOrdersByType(): Map<OrderType, Long> {
        val query = entityManager.createQuery(
            "SELECT o.type, COUNT(o) FROM Order o WHERE o.status IN :validStatuses GROUP BY o.type",
            Array<Any>::class.java
        )
        query.setParameter("validStatuses", OrderStatus.validStatuses)
        val resultList = query.resultList
        return resultList.associate { it[0] as OrderType to it[1] as Long }
    }

    override fun countOrdersByStatus(): Map<OrderStatus, Long> {
        val query = entityManager.createQuery(
            "SELECT o.status, COUNT(o) FROM Order o WHERE o.status IN :validStatuses GROUP BY o.status",
            Array<Any>::class.java
        )
        query.setParameter("validStatuses", OrderStatus.validStatuses)
        val resultList = query.resultList
        return resultList.associate { it[0] as OrderStatus to it[1] as Long }
    }

    override fun countOrdersByLastMonths(numberMonths: Int): Map<String, Long> {
        val startDate = ZonedDateTime.now().minusMonths(numberMonths.toLong()).withDayOfMonth(1)
        val query = entityManager.createQuery(
            "SELECT TO_CHAR(o.createdAt, 'YYYY/MM') as monthYear, COUNT(o) " +
                    "FROM Order o WHERE o.createdAt >= :startDate AND o.status IN :validStatuses " +
                    "GROUP BY monthYear " +
                    "ORDER BY TO_CHAR(o.createdAt, 'YYYY/MM')",
            Array<Any>::class.java
        )
        query.setParameter("validStatuses", OrderStatus.validStatuses)
        query.setParameter("startDate", startDate)
        val resultList = query.resultList
        return resultList.associate { (it[0] as String) to (it[1] as Long) }
    }

    override fun findAllActiveOrders(pageable: Pageable): Page<Order> {
        val query = entityManager.createQuery(
            "SELECT o FROM Order o WHERE o.status IN :validStatuses",
            Order::class.java
        )
        query.setParameter("validStatuses", OrderStatus.validStatuses)
        query.firstResult = pageable.offset.toInt()
        query.maxResults = pageable.pageSize

        val orders = query.resultList
        val countQuery = entityManager.createQuery(
            "SELECT COUNT(o) FROM Order o WHERE o.status IN :validStatuses",
            Long::class.javaObjectType
        )
        countQuery.setParameter("validStatuses", OrderStatus.validStatuses)
        val total = countQuery.singleResult

        return PageImpl(orders, pageable, total)
    }
}