package com.jlndev.di

import com.jlndev.baseservice.network.NetworkProviders
import com.jlndev.productservice.BuildConfig
import com.jlndev.productservice.data.api.ProductService
import com.jlndev.productservice.data.db.database.ProductDataBase
import com.jlndev.productservice.data.repository.CartRemoteRepository
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.OrderHistoryRemoteRepositoryImpl
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.OrderHistoryRepositoryImpl
import com.jlndev.productservice.data.repository.ProductRepository
import com.jlndev.productservice.data.repository.ProductRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productServiceModule = module {
    single { NetworkProviders.providerRetrofit(ProductService::class.java, BuildConfig.BASE_URL) }
    single { ProductDataBase.getInstance(androidContext()).cartDao }
    single { ProductDataBase.getInstance(androidContext()).orderHistoryDao }
    single { ProductDataBase.getInstance(androidContext()).productShowcaseDao }
    single<ProductRepository> { ProductRepositoryImpl(get(),get()) }

    //Remote
    single<CartRepository> { CartRemoteRepository(get(), get()) }
    single<OrderHistoryRepository> { OrderHistoryRemoteRepositoryImpl(get(), get()) }

    //Local
//    single<CartRepository> { CartRepositoryImpl(get()) }
//    single<OrderHistoryRepository> { OrderHistoryRepositoryImpl(get()) }
}