package com.jlndev.productservice.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jlndev.productservice.BuildConfig
import com.jlndev.productservice.data.db.dao.CartDao
import com.jlndev.productservice.data.db.dao.OrderHistoryDao
import com.jlndev.productservice.data.db.dao.ProductShowcaseDao
import com.jlndev.productservice.data.db.model.CartProductItemEntity
import com.jlndev.productservice.data.db.model.OrderHistoryEntity
import com.jlndev.productservice.data.db.model.ProductItemEntity

@Database(entities = [ProductItemEntity::class, OrderHistoryEntity::class, CartProductItemEntity::class], version = 1, exportSchema = false)
abstract class ProductDataBase : RoomDatabase() {

    abstract val cartDao: CartDao
    abstract val orderHistoryDao: OrderHistoryDao
    abstract val productShowcaseDao: ProductShowcaseDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDataBase? = null

//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.apply {
//                    execSQL("CREATE TABLE IF NOT EXISTS `NewProductsTable` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `price` REAL NOT NULL, `image` TEXT NOT NULL, `quantity` REAL NOT NULL, PRIMARY KEY(`id`))")
//                    execSQL("INSERT INTO NewProductsTable SELECT * FROM ProductsTable")
//                    execSQL("DROP TABLE ProductsTable")
//                    execSQL("ALTER TABLE NewProductsTable RENAME TO ProductsTable")
//                }
//            }
//        }
//
//        private val MIGRATION_2_3 = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.apply {
//                    execSQL("CREATE TABLE IF NOT EXISTS `NewCartTable` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `price` REAL NOT NULL, `image` TEXT NOT NULL, `quantity` REAL NOT NULL, PRIMARY KEY(`id`))")
//                    execSQL("INSERT INTO NewCartTable SELECT * FROM CartTable")
//                    execSQL("DROP TABLE CartTable")
//                    execSQL("ALTER TABLE NewCartTable RENAME TO CartTable")
//                }
//            }
//        }

        fun getInstance(context: Context): ProductDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDataBase::class.java,
                    BuildConfig.DATABASE_NAME
                )
//                    .addMigrations(MIGRATION_1_2)
//                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}