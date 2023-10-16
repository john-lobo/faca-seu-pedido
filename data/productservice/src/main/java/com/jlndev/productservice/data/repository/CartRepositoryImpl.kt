package com.jlndev.productservice.data.repository

import androidx.room.EmptyResultSetException
import com.jlndev.productservice.data.db.dao.CartDao
import com.jlndev.productservice.data.repository.ext.toCartProductItemEntity
import com.jlndev.productservice.data.repository.ext.toCartProductItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {
    override fun getProductsItems(): Single<List<ProductItemModel>> {
        return cartDao.getAllProductsItems()
            .flatMap { return@flatMap Single.just(it.map { response -> response.toCartProductItemModel() }) }
            .onErrorResumeNext { _ -> Single.just(listOf()) }
    }

    override fun insertProductItem(productItemModel: ProductItemModel): Completable {
        return cartDao.insertProductItem(productItemModel.toCartProductItemEntity())
    }

    override fun deleteProductItem(productItemModel: ProductItemModel): Completable {
        return cartDao.deleteProductItem(productItemModel.id)
    }

    override fun deleteAllProductsItems(): Completable {
        return cartDao.deleteAllProductsItems()
    }

    override fun getProductItem(productItemModel: ProductItemModel): Single<ProductItemModel> {
        return cartDao.getProductItem(productItemModel.id)
            .flatMap {
                it.quantity += productItemModel.quantity
                return@flatMap cartDao.insertProductItem(it)
                        .andThen(Single.just(productItemModel))
                        .onErrorResumeNext { Single.error(it) }
                }
            .onErrorResumeNext { error ->
                if (error is EmptyResultSetException) {
                    cartDao.insertProductItem(productItemModel.toCartProductItemEntity())
                        .andThen(Single.just(productItemModel))
                        .onErrorResumeNext { Single.error(it) }
                } else {
                    Single.error(error)
                }
            }
    }
}