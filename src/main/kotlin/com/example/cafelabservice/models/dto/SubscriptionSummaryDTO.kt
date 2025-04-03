package com.example.cafelabservice.models.dto

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.Subscription
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class SubscriptionResponseDTO(
    val id: Long,
    val order: OrderResponseDTO?,
    val name: String,
    val periodicity: Int,
    val description: String?,
    val image: String?,
    val periodicityString: String?,
    val price: String?,
    val nextInvoice: String? = null,
)

/**
 * Converts a [Subscription] entity to a [SubscriptionResponseDTO].
 *
 * @return A [SubscriptionResponseDTO] representation of the subscription.
 */
fun Subscription.toSubscriptionResponseDTO(order: Order? = null): SubscriptionResponseDTO {
    val today = LocalDate.now()
    val nextDay28 = if (today.dayOfMonth <= 28) {
        today.withDayOfMonth(28)
    } else {
        today.plusMonths(1).withDayOfMonth(28)
    }
    val nextInvoiceFormatted = nextDay28.format(DateTimeFormatter.ISO_DATE)
    val orderResponseDTO = order?.toOrderResponseDTO()

    return SubscriptionResponseDTO(
        id = id,
        order = orderResponseDTO,
        name = name!!,
        periodicity = periodicity ?: 0,
        description = description,
        image = image,
        periodicityString = periodicityString,
        price = price,
        nextInvoice = nextInvoiceFormatted,
    )
}

