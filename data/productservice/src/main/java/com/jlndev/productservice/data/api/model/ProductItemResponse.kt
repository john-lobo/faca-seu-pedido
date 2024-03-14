package com.jlndev.productservice.data.api.model

import com.jlndev.productservice.data.db.model.ProductItemEntity
import com.jlndev.productservice.data.repository.model.ProductItemModel

data class ProductItemResponse(
    val id: Int?,
    val title: String?,
    val description: String?,
    val price: Double?,
    val image: String?
) {
    fun toProductItemModel(): ProductItemModel {
        return ProductItemModel(
            id ?: 0,
            title.orEmpty(),
            description.orEmpty(),
            price ?: 0.0,
            image.orEmpty(),
            0
        )
    }

    fun toProductItemEntity(): ProductItemEntity {
        return ProductItemEntity(
            id ?: 0,
            title.orEmpty(),
            description.orEmpty(),
            price ?: 0.0,
            image.orEmpty()
        )
    }

}