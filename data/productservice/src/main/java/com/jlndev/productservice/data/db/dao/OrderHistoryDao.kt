package com.jlndev.productservice.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jlndev.productservice.data.db.model.OrderHistoryEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface OrderHistoryDao {

    @Insert
    fun insertOrder(orderItem: OrderHistoryEntity) : Completable

    @Query("SELECT * FROM OrderHistoryTable")
    fun getAllOrders(): Single<List<OrderHistoryEntity>>
}