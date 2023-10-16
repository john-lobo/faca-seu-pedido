package com.jlndev.productservice.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jlndev.productservice.data.db.model.ProductItemEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductShowcaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductsItems(productsItems: List<ProductItemEntity>) : Completable

    @Query("SELECT * FROM ProductsTable")
    fun getAllProductsItems(): Single<List<ProductItemEntity>>
}