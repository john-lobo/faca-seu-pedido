package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Single

interface ProductRepository {
    fun getProductsItems  (): Single<List<ProductItemModel>>
}