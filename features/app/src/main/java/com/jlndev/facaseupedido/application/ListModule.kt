package com.jlndev.facaseupedido.application

import com.jlndev.di.productServiceModule
import com.jlndev.facaseupedido.di.viewModelModule
import org.koin.core.module.Module

val modules = arrayListOf(
    productServiceModule,
    viewModelModule
)