package com.jlndev.facaseupedido.application

import android.app.Application
import com.jlndev.facaseupedido.di.viewModelModule
import com.jlndev.productservice.di.productServiceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            val modules = arrayListOf<Module>().apply {
                add(productServiceModule)
                add(viewModelModule)
            }
            modules(modules)
        }
    }
}