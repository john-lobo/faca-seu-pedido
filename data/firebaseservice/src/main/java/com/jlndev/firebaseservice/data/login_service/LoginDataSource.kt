package com.jlndev.firebaseservice.data.login_service

import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

interface LoginDataSource {
    fun login(email: String, password: String) : Single<User>
    fun loginWithGoogle(idToken: String) : Single<User>
}