package com.jlndev.facaseupedido.ui.login.usecase

import com.jlndev.firebaseservice.data.login_service.LoginRepository
import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

class LoginUseCaseImpl(
    private val repository: LoginRepository,
) : LoginUseCase {
    override fun login(email: String, password: String): Single<User> {
        return repository.login(email, password)
    }

    override fun loginWithGoogle(idToken: String): Single<User> {
        return repository.loginWithGoogle(idToken)
    }
}