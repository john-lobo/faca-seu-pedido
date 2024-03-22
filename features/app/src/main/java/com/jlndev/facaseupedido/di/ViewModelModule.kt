package com.jlndev.facaseupedido.di

import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.SchedulerProvider
import com.jlndev.facaseupedido.ui.cart.CartViewModel
import com.jlndev.facaseupedido.ui.home.HomeViewModel
import com.jlndev.facaseupedido.ui.item.DetailsViewModel
import com.jlndev.facaseupedido.ui.login.LoginViewModel
import com.jlndev.facaseupedido.ui.orderhistory.OrderHistoryViewModel
import com.jlndev.facaseupedido.ui.recover_password.RecoverPasswordViewModel
import com.jlndev.facaseupedido.ui.register_user.RegisterUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single <BaseSchedulerProvider>{ SchedulerProvider() }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { CartViewModel(get(), get(), get()) }
    viewModel { OrderHistoryViewModel(get(), get()) }
    viewModel { DetailsViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RecoverPasswordViewModel(get(), get()) }
    viewModel { RegisterUserViewModel(get(), get()) }
}