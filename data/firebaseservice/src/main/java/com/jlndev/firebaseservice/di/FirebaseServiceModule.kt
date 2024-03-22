package com.jlndev.firebaseservice.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.firebaseservice.data.login_service.LoginDataSource
import com.jlndev.firebaseservice.data.login_service.LoginFirebaseDataSource
import com.jlndev.firebaseservice.data.login_service.LoginRepository
import com.jlndev.firebaseservice.data.recover_password_service.RecoverPasswordDataSource
import com.jlndev.firebaseservice.data.recover_password_service.RecoverPasswordFirebaseDataSource
import com.jlndev.firebaseservice.data.recover_password_service.RecoverPasswordRepository
import com.jlndev.firebaseservice.data.register_user_service.RegisterUserDataSource
import com.jlndev.firebaseservice.data.register_user_service.RegisterUserFirebaseDataSource
import com.jlndev.firebaseservice.data.register_user_service.RegisterUserRepository
import org.koin.dsl.module

val firebaseServiceModule = module {
    single<RecoverPasswordDataSource> { RecoverPasswordFirebaseDataSource(get()) }
    single { RecoverPasswordRepository(get()) }

    single<LoginDataSource> { LoginFirebaseDataSource(get(), get()) }
    single { LoginRepository(get()) }

    single<RegisterUserDataSource> { RegisterUserFirebaseDataSource(get(), get()) }
    single { RegisterUserRepository(get()) }

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}