package com.jlndev.facaseupedido.ui.register_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.montapp.montapp.view.ui.register_user.use_case.RegisterUserUseCase
import com.jlndev.firebaseservice.model.User

class RegisterUserViewModel(
    private val useCase: RegisterUserUseCase,
    private val schedulerProvider: BaseSchedulerProvider
) : BaseViewModel() {

    private val _registerUserLive = MutableLiveData<ResponseState<User>>()
    val registerUserLive: LiveData<ResponseState<User>>
        get() = _registerUserLive

    fun registerUser(user: User) {
        useCase.registerUser(user)
            .processSingle(schedulerProvider)
            .doOnSubscribe { _registerUserLive.value = ResponseState.Loading(true) }
            .doFinally { _registerUserLive.value = ResponseState.Loading(false) }
            .doOnSuccess { _registerUserLive.value = ResponseState.Success(it) }
            .doOnError { _registerUserLive.value = ResponseState.Error(it) }
            .disposedBy(disposables)

    }

}