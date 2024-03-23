package com.jlndev.productservice.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class UserOrderHistory (
    val id: String = "",
    val order: String = "",
    val quantityItems: Int = 0,
    val totalValue: Double = 0.0
)