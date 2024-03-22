package com.jlndev.facaseupedido.di

import com.jlndev.facaseupedido.ui.login.usecase.LoginUseCase
import com.jlndev.facaseupedido.ui.login.usecase.LoginUseCaseImpl
import com.jlndev.facaseupedido.ui.recover_password.usecase.RecoverPasswordUseCase
import com.jlndev.facaseupedido.ui.recover_password.usecase.RecoverPasswordUseCaseImpl
import com.montapp.montapp.view.ui.register_user.use_case.RegisterUserUseCase
import com.jlndev.facaseupedido.ui.register_user.use_case.RegisterUserUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<LoginUseCase> { LoginUseCaseImpl(get()) }
    single<RecoverPasswordUseCase> { RecoverPasswordUseCaseImpl(get()) }
    single<RegisterUserUseCase> { RegisterUserUseCaseImpl(get()) }
}