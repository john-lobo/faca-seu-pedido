package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

interface CartRepository {
    fun getProductsItems  (): Single<List<ProductItemModel>>
    fun insertProductItem  (productItemModel: ProductItemModel): Completable
    fun deleteProductItem  (productItemModel: ProductItemModel): Completable
    fun deleteAllProductsItems() : Completable
    fun getProductItem(productItemModel: ProductItemModel) : Single<ProductItemModel>
}