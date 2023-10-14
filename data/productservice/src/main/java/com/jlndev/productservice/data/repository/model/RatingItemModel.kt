package com.jlndev.productservice.data.repository.model

import com.google.gson.annotations.SerializedName

data class RatingItemModel(
    val ratingQuantity: Int,
    val rating: Double
)