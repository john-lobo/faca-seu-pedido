package com.jlndev.productservice.data.remote.model

data class CartTotalQuantity(
    var totalQuantity: Long = 1,
    val totalPrice: Double = 0.0,
)