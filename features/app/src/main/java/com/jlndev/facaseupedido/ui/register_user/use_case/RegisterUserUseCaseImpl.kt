package com.jlndev.facaseupedido.ui.register_user.use_case

import com.jlndev.firebaseservice.data.register_user_service.RegisterUserRepository
import com.jlndev.firebaseservice.model.User
import com.montapp.montapp.view.ui.register_user.use_case.RegisterUserUseCase
import io.reactivex.Single

class RegisterUserUseCaseImpl(private val repository: RegisterUserRepository) :
    RegisterUserUseCase {

    override fun registerUser(user: User): Single<User> {
        return repository.registerUser(user)
    }
}