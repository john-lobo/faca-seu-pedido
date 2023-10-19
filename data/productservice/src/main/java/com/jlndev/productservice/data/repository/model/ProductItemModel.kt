package com.jlndev.productservice.data.repository.model

data class ProductItemModel(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Double = 1.0
)