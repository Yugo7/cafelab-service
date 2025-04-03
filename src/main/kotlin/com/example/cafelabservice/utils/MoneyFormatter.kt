package com.example.cafelabservice.utils

import java.text.NumberFormat
import java.util.Locale

class MoneyFormatter {
    companion object {
        fun formatToEuros(amountInCents: String): String {
            val amountInEuros = amountInCents.toDouble() / 100
            val numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY)
            return numberFormat.format(amountInEuros)
        }
    }
}