package com.jlndev.firebaseservice.data.register_user_service

import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

class RegisterUserRepository (private val dataSource: RegisterUserDataSource) {
    fun registerUser(user: User): Single<User> {
        return dataSource.registerUser(user)
    }
}