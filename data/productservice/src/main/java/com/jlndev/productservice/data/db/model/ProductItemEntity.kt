package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jlndev.productservice.data.repository.model.ProductItemModel

@Entity(tableName = "ProductsTable")
data class ProductItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Long = 1
) {
    fun toProductItemModel(): ProductItemModel {
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