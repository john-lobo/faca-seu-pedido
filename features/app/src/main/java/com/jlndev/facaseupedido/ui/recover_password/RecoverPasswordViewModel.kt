package com.jlndev.facaseupedido.ui.recover_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processCompletable
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.facaseupedido.ui.recover_password.usecase.RecoverPasswordUseCase

class RecoverPasswordViewModel(
    private val useCase: RecoverPasswordUseCase,
    private val schedulerProvider: BaseSchedulerProvider
    ) : BaseViewModel() {

    private val _recoverPasswordLive = MutableLiveData<ResponseState<Unit>>()
    val recoverPasswordLive: LiveData<ResponseState<Unit>>
        get() = _recoverPasswordLive

    fun sendRecoveryCodeForEmail(email: String) {
        useCase.sendRecoveryCodeForEmail(email)
            .processCompletable(schedulerProvider)
            .doOnSubscribe { _recoverPasswordLive.value = ResponseState.Loading(true) }
            .doFinally { _recoverPasswordLive.value = ResponseState.Loading(false) }
            .doOnComplete { _recoverPasswordLive.value = ResponseState.Success(Unit) }
            .doOnError { _recoverPasswordLive.value = ResponseState.Error(it) }
            .disposedBy(disposables)
    }
}