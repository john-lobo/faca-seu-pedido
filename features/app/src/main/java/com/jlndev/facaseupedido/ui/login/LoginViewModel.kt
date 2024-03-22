package com.jlndev.facaseupedido.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.facaseupedido.ui.login.usecase.LoginUseCase
import com.jlndev.firebaseservice.model.User

class LoginViewModel(private val loginUseCase: LoginUseCase, private val scheduler: BaseSchedulerProvider) : BaseViewModel() {

    private val _loginLive = MutableLiveData<ResponseState<User>>()
    val loginLive: LiveData<ResponseState<User>>
        get() = _loginLive

    private val _loginGoogleLive = MutableLiveData<ResponseState<User>>()
    val loginGoogleLive: LiveData<ResponseState<User>>
        get() = _loginGoogleLive

    fun login(email: String, password: String) {
        loginUseCase.login(email, password)
            .processSingle(scheduler)
            .doOnSubscribe { _loginLive.value = ResponseState.Loading(true) }
            .doFinally { _loginLive.value = ResponseState.Loading(false) }
            .doOnSuccess { user ->
                _loginLive.value = ResponseState.Success(user)
            }
            .doOnError  { error ->
                _loginLive.value = ResponseState.Error(error)
            }
            .disposedBy(disposables)
    }

    fun loginWithGoogle(idToken: String) {
        loginUseCase.loginWithGoogle(idToken)
            .processSingle(scheduler)
            .doOnSubscribe { _loginGoogleLive.value = ResponseState.Loading(true) }
            .doFinally { _loginGoogleLive.value = ResponseState.Loading(false) }
            .doOnSuccess { user ->
                _loginGoogleLive.value = ResponseState.Success(user)
            }
            .doOnError  { error ->
                _loginGoogleLive.value = ResponseState.Error(error)
            }
            .disposedBy(disposables)
    }
}