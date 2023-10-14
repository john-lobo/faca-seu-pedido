package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductItemEntity")
data class ProductItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val ratingQuantity: Int,
    val rating: Double,
    val quantity: Int
)