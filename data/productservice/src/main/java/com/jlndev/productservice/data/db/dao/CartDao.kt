package com.jlndev.productservice.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jlndev.productservice.data.db.model.CartProductItemEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductItem(productItem: CartProductItemEntity) : Completable

    @Query("SELECT * FROM CartTable")
    fun getAllProductsItems(): Single<List<CartProductItemEntity>>

    @Query("DELETE FROM CartTable WHERE id = :productId")
    fun deleteProductItem(productId: Int) : Completable

    @Query("DELETE FROM CartTable")
    fun deleteAllProductsItems() : Completable

    @Query("SELECT * FROM CartTable WHERE id = :productId")
    fun getProductItem(productId: Int) : Single<CartProductItemEntity>
}