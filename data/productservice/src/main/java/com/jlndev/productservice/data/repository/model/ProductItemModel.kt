package com.jlndev.productservice.data.repository.model

import com.jlndev.productservice.data.db.model.CartProductItemEntity
import com.jlndev.productservice.data.remote.model.UserCartProductItem
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItemModel(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val image: String = "",
    var quantity: Long = 1
) : Parcelable {
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

    fun toUserCartProductItem(): UserCartProductItem {
        return UserCartProductItem(
            id,
            title,
            description,
            price,
            image,
            quantity
        )
    }
}