package com.jlndev.facaseupedido.ui.cart.adapter

import com.jlndev.coreandroid.bases.adapter.BaseAdapterListener
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

interface CartProductAdapterListener : BaseAdapterListener<ProductItem> {
    fun updateProductToCart(productItem : ProductItem)
    fun deleteProductToCart(productItem : ProductItem)
}