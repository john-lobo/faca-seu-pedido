package com.jlndev.facaseupedido.ui.orderhistory.adapter

import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

interface OrderHistoryAdapterListener {
    fun clickedProductItem(item: ProductItem)
}