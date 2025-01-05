package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>