package com.jlndev.productservice.data.remote.model

import androidx.room.PrimaryKey
import com.jlndev.productservice.data.db.model.CartProductItemEntity

data class UserCart(
    @PrimaryKey val userId: String? = null, // ID do usuário, para associar o carrinho a um usuário específico
    val productItems: List<UserCartProductItem> = emptyList()
)
