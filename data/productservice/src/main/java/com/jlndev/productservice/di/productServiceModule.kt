package com.jlndev.productservice.di

import com.jlndev.baseservice.network.NetworkProviders
import com.jlndev.productservice.BuildConfig
import com.jlndev.productservice.data.api.ProductService
import com.jlndev.productservice.data.db.database.ProductDataBase
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.CartRepositoryImpl
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.OrderHistoryRepositoryImpl
import com.jlndev.productservice.data.repository.ProductRepository
import com.jlndev.productservice.data.repository.ProductRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val productServiceModule = module {
    single { NetworkProviders.providerRetrofit(ProductService::class.java, BuildConfig.BASE_URL) }
    single { ProductDataBase.getInstance(androidContext()).cartDao }
    single { ProductDataBase.getInstance(androidContext()).orderHistoryDao }
    single { ProductDataBase.getInstance(androidContext()).productShowcaseDao }
    single<ProductRepository> { ProductRepositoryImpl(get(),get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<OrderHistoryRepository> { OrderHistoryRepositoryImpl(get(), get()) }
}