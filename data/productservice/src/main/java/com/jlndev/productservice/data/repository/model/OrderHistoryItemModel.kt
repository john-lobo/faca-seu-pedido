package com.jlndev.productservice.data.repository.model

data class OrderHistoryItemModel(
    val id: Long,
    val productsItems: List<ProductItemModel>,
    val quantityItems: Int,
    val totalValue: Double
)