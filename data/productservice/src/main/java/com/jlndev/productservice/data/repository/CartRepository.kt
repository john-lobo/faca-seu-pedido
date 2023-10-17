package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

interface CartRepository {
    fun getProductsItems  (): Single<List<ProductItemModel>>
    fun insertProductItem  (productItemModel: ProductItemModel): Single<ProductItemModel>
    fun deleteProductItem  (productItemModel: ProductItemModel): Single<ProductItemModel>
    fun deleteAllProductsItems() : Single<List<ProductItemModel>>
    fun getProductItem(productItemModel: ProductItemModel) : Single<ProductItemModel>
}