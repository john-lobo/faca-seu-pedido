package com.jlndev.productservice.data.repository

import com.google.gson.Gson
import com.jlndev.productservice.data.db.dao.OrderHistoryDao
import com.jlndev.productservice.data.db.model.OrderHistoryEntity
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.reactivex.Completable
import io.reactivex.Single

class OrderHistoryRepositoryImpl(
    private val cartDao: OrderHistoryDao
) : OrderHistoryRepository {

    override fun getAllOrders(): Single<List<OrderHistoryItemModel>> {
        return cartDao.getAllOrders().flatMap { response ->
            Single.fromCallable {
                response.map { orderHistory ->
                    val order = Gson().fromJson(orderHistory.order, Array<ProductItemModel>::class.java).asList()
                    OrderHistoryItemModel(
                        orderHistory.id,
                        order,
                        orderHistory.totalValue
                    )
                }
            }
        }.onErrorResumeNext {
            Single.just(emptyList())
        }
    }

    override fun insertOrder(productsItems: List<ProductItemModel>): Completable {
        val orderItem = Gson().toJson(productsItems)
        val totalValue = productsItems.sumOf { it.price * it.quantity }
        return cartDao.insertOrder(OrderHistoryEntity(order = orderItem, totalValue = totalValue))
    }
}