package com.jlndev.productservice.data.repository

import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

interface OrderHistoryRepository {
    fun insertOrder(productsItems: List<ProductItemModel>): Completable
    fun getAllOrders(): Single<List<OrderHistoryItemModel>>
}