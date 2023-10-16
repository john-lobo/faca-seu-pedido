package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.api.ProductService
import com.jlndev.productservice.data.db.dao.ProductShowcaseDao
import com.jlndev.productservice.data.repository.ext.toProductItemEntity
import com.jlndev.productservice.data.repository.ext.toProductItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Single

class ProductRepositoryImpl(
    private val service: ProductService,
    private val productShowcaseDao: ProductShowcaseDao
) : ProductRepository {
    override fun getProductsItems(): Single<List<ProductItemModel>> {
        return productShowcaseDao.getAllProductsItems()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap getAndSaveProductsItems()
                } else {
                    return@flatMap Single.just(it.map { response ->
                        response.toProductItemModel()
                    })
                }}
            .onErrorResumeNext { _ ->
                getAndSaveProductsItems()
            }
    }

    private fun getAndSaveProductsItems(): Single<List<ProductItemModel>> {
        return service.getProductsItems()
            .flatMap { response ->
                productShowcaseDao.insertProductsItems(response.map { it.toProductItemEntity() })
                    .toSingleDefault(response.map { it.toProductItemModel() })
            }
    }
}