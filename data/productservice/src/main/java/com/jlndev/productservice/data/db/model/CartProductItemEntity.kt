package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jlndev.productservice.data.repository.model.ProductItemModel

@Entity(tableName = "CartTable")
data class CartProductItemEntity(
    @PrimaryKey
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