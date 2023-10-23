package com.jlndev.facaseupedido.ui.home.adapter

import com.jlndev.coreandroid.bases.adapter.BaseAdapterListener
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

interface ProductAdapterListener : BaseAdapterListener<ProductItem> {
    fun addProductToCart(productItem : ProductItem)
}