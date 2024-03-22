package com.jlndev.facaseupedido.application

import com.jlndev.di.productServiceModule
import com.jlndev.facaseupedido.di.useCaseModule
import com.jlndev.facaseupedido.di.viewModelModule
import com.jlndev.firebaseservice.di.firebaseServiceModule

val modules = arrayListOf(
    productServiceModule,
    viewModelModule,
    useCaseModule,
    firebaseServiceModule
)