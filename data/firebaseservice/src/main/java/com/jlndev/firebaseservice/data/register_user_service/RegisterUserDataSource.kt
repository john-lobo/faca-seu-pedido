package com.jlndev.firebaseservice.data.register_user_service

import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

interface RegisterUserDataSource {
    fun registerUser(user: User): Single<User>
}