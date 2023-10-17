package com.jlndev.facaseupedido.ui.uitls.model

import com.jlndev.coreandroid.bases.adapter.BaseDiffItemView

data class OrderHistoryItem(
    override val id: String,
    val productsItems: List<ProductItem>,
    val quantityItems: Int,
    val totalValue: Double
): BaseDiffItemView()