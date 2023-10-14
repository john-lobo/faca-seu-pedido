package com.jlndev.productservice.data.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jlndev.productservice.data.db.model.OrderHistoryEntity
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

interface OrderHistoryRepository {
    fun insertOrder(productsItem: List<ProductItemModel>): Completable
    fun getAllOrders(): Single<List<OrderHistoryItemModel>>
}