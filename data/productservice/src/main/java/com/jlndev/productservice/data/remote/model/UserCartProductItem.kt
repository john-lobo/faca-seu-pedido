package com.jlndev.productservice.data.remote.model

import com.jlndev.productservice.data.repository.model.ProductItemModel

data class UserCartProductItem (
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Long = 1
) {
    fun toCartProductItemModel(): ProductItemModel {
        return ProductItemModel(
            id,
            title,
            description,
            price,
            image,
            quantity
        )
    }
}