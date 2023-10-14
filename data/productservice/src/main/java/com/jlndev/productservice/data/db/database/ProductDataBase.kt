package com.jlndev.productservice.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jlndev.productservice.BuildConfig
import com.jlndev.productservice.data.db.dao.CartDao
import com.jlndev.productservice.data.db.dao.OrderHistoryDao
import com.jlndev.productservice.data.db.dao.ProductShowcaseDao
import com.jlndev.productservice.data.db.model.ProductItemEntity

@Database(entities = [ProductItemEntity::class], version = 1, exportSchema = false)
abstract class ProductDataBase: RoomDatabase() {

    abstract val cartDao: CartDao
    abstract val orderHistoryDao: OrderHistoryDao
    abstract val productShowcaseDao: ProductShowcaseDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDataBase? = null

        fun getInstance(context: Context): ProductDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDataBase::class.java,
                    BuildConfig.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}