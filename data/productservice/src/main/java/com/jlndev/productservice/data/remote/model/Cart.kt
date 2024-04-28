package com.jlndev.productservice.data.remote.model

import com.jlndev.productservice.data.repository.model.ProductItemModel

data class Cart(
    val productItems: List<ProductItemModel> = emptyList(),
    val totalQuantity: Long = 0,
    val totalPrice: Double = 0.0
)

