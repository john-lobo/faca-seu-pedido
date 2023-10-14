package com.jlndev.productservice.data.api.model

data class ProductItemResponse(
    val id: Int?,
    val title: String?,
    val description: String?,
    val price: Double?,
    val image: String?,
    val rating: RatingItemResponse?,
)