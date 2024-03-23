package com.jlndev.productservice.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class UserOrderHistory (
    val id: String = "",
    val order: String = "",
    val totalQuantity: Int = 0,
    val totalPrice: Double = 0.0
)