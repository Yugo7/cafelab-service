package com.example.cafelabservice.models

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.Subscription

data class OrderSubscription(
    val order: Order,
    val subscription: Subscription?,
    )