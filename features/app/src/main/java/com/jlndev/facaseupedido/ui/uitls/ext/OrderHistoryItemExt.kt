package com.jlndev.facaseupedido.ui.uitls.ext

import com.jlndev.facaseupedido.ui.uitls.model.OrderHistoryItem
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel

fun OrderHistoryItemModel.toOrderHistoryItem(): OrderHistoryItem {
    return OrderHistoryItem(
        id.toString(),
        productsItems.map { it.toProductItem() },
        quantityItems,
        totalValue)
}
