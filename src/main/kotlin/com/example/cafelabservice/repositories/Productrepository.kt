package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>{
    fun findAllByIsActiveTrue(): List<Product>
}