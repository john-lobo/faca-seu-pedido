package com.jlndev.firebaseservice.data.login_service

import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

class LoginRepository (private val dataSource: LoginDataSource) {
    fun login(email: String, password: String) : Single<User> {
        return dataSource.login(email, password)
    }

    fun loginWithGoogle(idToken: String) : Single<User> {
        return dataSource.loginWithGoogle(idToken)
    }
}