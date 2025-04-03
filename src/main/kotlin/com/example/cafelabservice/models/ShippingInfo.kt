package com.example.cafelabservice.models

data class ShippingInfo(
    val address: String?,
    val city: String?,
    val state: String?,
    val zip: String?,
    val country: String?,
    val gift: Boolean,
    val shippingCost: String?,
    val line2: String?
){
    fun getShippingAddressString(): String {
        return "$address<br />$line2<br /> $city, $state - $country<br />$zip"
    }
}