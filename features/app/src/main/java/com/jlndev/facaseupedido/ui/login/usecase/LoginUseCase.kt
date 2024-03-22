package com.jlndev.facaseupedido.ui.login.usecase

import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

interface LoginUseCase {
    fun login(email: String, password: String) : Single<User>

    fun loginWithGoogle(idToken: String): Single<User>
}