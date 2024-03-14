package com.jlndev.productservice.data.repository.model

import com.jlndev.productservice.data.db.model.CartProductItemEntity

data class ProductItemModel(
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

    fun toCartProductItemEntity(): CartProductItemEntity {
        return CartProductItemEntity(
            id,
            title,
            description,
            price,
            image,
            quantity
        )
    }

}