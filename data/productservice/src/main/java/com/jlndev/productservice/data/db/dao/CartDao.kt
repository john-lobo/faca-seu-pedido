package com.jlndev.productservice.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jlndev.productservice.data.db.model.ProductItemEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductItem(productItem: ProductItemEntity) : Completable

    @Query("SELECT * FROM ProductItemEntity")
    fun getAllProductsItems(): Single<List<ProductItemEntity>>

    @Query("DELETE FROM ProductItemEntity WHERE id = :productId")
    fun deleteProductItem(productId: Int) : Completable

    @Query("DELETE FROM ProductItemEntity")
    fun deleteAllProductsItems() : Completable
}