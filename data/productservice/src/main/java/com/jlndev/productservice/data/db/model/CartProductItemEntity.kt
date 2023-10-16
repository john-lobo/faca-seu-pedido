package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartTable")
data class CartProductItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Int = 1
)