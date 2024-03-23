package com.jlndev.productservice.data.repository.model

data class OrderHistoryItemModel(
    val id: String = "",
    val productsItems: List<ProductItemModel> = listOf(),
    val quantityItems: Int = 0,
    val totalValue: Double = 0.0
)