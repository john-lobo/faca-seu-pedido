package com.jlndev.productservice.data.api.model

import com.google.gson.annotations.SerializedName

data class RatingItemResponse(
    @SerializedName("count")
    val ratingQuantity: Int,
    @SerializedName("rate")
    val rating: Double
)