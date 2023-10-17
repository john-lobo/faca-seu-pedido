package com.jlndev.facaseupedido.ui.uitls.ext

import com.jlndev.facaseupedido.ui.uitls.model.ProductItem
import com.jlndev.productservice.data.repository.model.ProductItemModel

fun ProductItemModel.toProductItem(): ProductItem {
    return ProductItem(
        id.toString(),
        title,
        description,
        price,
        image,
        quantity
    )
}

fun ProductItem.toProductItemModel(): ProductItemModel {
    return ProductItemModel(
        id.toInt(),
        title,
        description,
        price,
        image,
        quantity
    )
}
