package com.jlndev.productservice.data.repository.ext

import com.jlndev.productservice.data.api.model.ProductItemResponse
import com.jlndev.productservice.data.db.model.CartProductItemEntity
import com.jlndev.productservice.data.db.model.ProductItemEntity
import com.jlndev.productservice.data.repository.model.ProductItemModel

fun ProductItemResponse.toProductItemModel(): ProductItemModel {
    return ProductItemModel(
        id ?: 0,
        title.orEmpty(),
        description.orEmpty(),
        price ?: 0.0,
        image.orEmpty(),
        0
    )
}

fun ProductItemResponse.toProductItemEntity(): ProductItemEntity {
    return ProductItemEntity(
        id ?: 0,
        title.orEmpty(),
        description.orEmpty(),
        price ?: 0.0,
        image.orEmpty()
    )
}

fun ProductItemEntity.toProductItemModel(): ProductItemModel {
    return ProductItemModel(
        id,
        title,
        description,
        price,
        image,
        quantity
    )
}

fun CartProductItemEntity.toCartProductItemModel(): ProductItemModel {
    return ProductItemModel(
        id,
        title,
        description,
        price,
        image,
        quantity
    )
}

fun ProductItemModel.toProductItemEntity(): ProductItemEntity {
    return ProductItemEntity(
        id,
        title,
        description,
        price,
        image,
        quantity
    )
}

fun ProductItemModel.toCartProductItemEntity(): CartProductItemEntity {
    return CartProductItemEntity(
        id,
        title,
        description,
        price,
        image,
        quantity
    )
}