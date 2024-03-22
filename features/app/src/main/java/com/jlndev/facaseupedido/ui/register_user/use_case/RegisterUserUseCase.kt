package com.montapp.montapp.view.ui.register_user.use_case

import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

interface RegisterUserUseCase {
    fun registerUser(user: User): Single<User>
}