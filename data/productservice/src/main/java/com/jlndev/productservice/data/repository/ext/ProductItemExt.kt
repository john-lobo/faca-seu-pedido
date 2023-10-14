package com.jlndev.productservice.data.repository.ext

import com.jlndev.productservice.data.api.model.ProductItemResponse
import com.jlndev.productservice.data.api.model.RatingItemResponse
import com.jlndev.productservice.data.db.model.ProductItemEntity
import com.jlndev.productservice.data.repository.model.ProductItemModel
import com.jlndev.productservice.data.repository.model.RatingItemModel

fun ProductItemResponse.toProductItemModel(): ProductItemModel {
    return ProductItemModel(
        id ?: 0, // Provide a default value if id is null
        title ?: "", // Provide a default value if title is null
        description ?: "", // Provide a default value if description is null
        price ?: 0.0, // Provide a default value if price is null
        image ?: "", // Provide a default value if image is null
        rating?.toRatingItemModel() ?: RatingItemModel(0, 0.0) // Use a default RatingItemModel if rating is null
    )
}

fun ProductItemResponse.toProductItemEntity(): ProductItemEntity {
    return ProductItemEntity(
        id ?: 0, // Provide a default value if id is null
        title ?: "", // Provide a default value if title is null
        description ?: "", // Provide a default value if description is null
        price ?: 0.0, // Provide a default value if price is null
        image ?: "", // Provide a default value if image is null
        rating?.ratingQuantity ?: 0, // Use a default value if rating or ratingQuantity is null
        rating?.rating ?: 0.0, // Use a default value if rating or rating is null
        0
    )
}

fun ProductItemEntity.toProductItemModel(): ProductItemModel {
    return ProductItemModel(
        id,
        title,
        description,
        price,
        image,
        RatingItemModel(ratingQuantity, rating)
    )
}

fun ProductItemModel.toProductItemEntity(quantity: Int): ProductItemEntity {
    return ProductItemEntity(
        id,
        title,
        description,
        price,
        image,
        rating.ratingQuantity,
        rating.rating,
        quantity
    )
}

fun RatingItemResponse.toRatingItemModel(): RatingItemModel {
    return RatingItemModel(ratingQuantity, rating)
}