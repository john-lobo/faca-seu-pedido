package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.db.dao.CartDao
import com.jlndev.productservice.data.repository.ext.toProductItemEntity
import com.jlndev.productservice.data.repository.ext.toProductItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {
    override fun getProductsItems(): Single<List<ProductItemModel>> {
        return cartDao.getAllProductsItems()
            .flatMap { return@flatMap Single.just(it.map { response -> response.toProductItemModel()}) }
            .onErrorResumeNext { _ -> Single.just(listOf()) }
    }

    override fun insertProductItem(productItemModel: ProductItemModel, quantity: Int): Completable {
        return cartDao.insertProductItem(productItemModel.toProductItemEntity(quantity))
    }

    override fun deleteProductItem(productItemModel: ProductItemModel): Completable {
        return cartDao.deleteProductItem(productItemModel.id)
    }

    override fun deleteAllProductsItems(): Completable {
        return cartDao.deleteAllProductsItems()
    }
}