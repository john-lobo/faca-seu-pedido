package com.jlndev.productservice.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jlndev.productservice.data.db.model.OrderHistoryEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface OrderHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(orderItem: OrderHistoryEntity) : Completable

    @Query("SELECT * FROM OrderEntity")
    fun getAllOrders(): Single<List<OrderHistoryEntity>>
}