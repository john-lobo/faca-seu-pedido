package com.jlndev.productservice.data.repository.model

data class ProductItemModel(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Long = 1
)