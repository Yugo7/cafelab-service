package com.example.cafelabservice.utils.converters

import com.example.cafelabservice.models.OrderProduct
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonbListProductConverter : AttributeConverter<List<OrderProduct>, String> {
    private val objectMapper = jacksonObjectMapper()

    override fun convertToEntityAttribute(dbData: String?): List<OrderProduct> {
        return dbData?.let { objectMapper.readValue(it, Array<OrderProduct>::class.java).toList() } ?: emptyList()
    }

    override fun convertToDatabaseColumn(attribute: List<OrderProduct>?): String {
        return attribute?.let { objectMapper.writeValueAsString(it) } ?: "[]"
    }
}