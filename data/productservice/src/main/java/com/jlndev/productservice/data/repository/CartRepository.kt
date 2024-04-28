package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.remote.model.Cart
import com.jlndev.productservice.data.remote.model.CartTotalQuantity
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Single

interface CartRepository {
    fun getProductsItems  (): Single<Cart>
    fun insertProductItem  (product: ProductItemModel): Single<Cart>
    fun deleteProductItem  (product: ProductItemModel): Single<Cart>
    fun deleteAllProductsItems() :Single<Cart>
}