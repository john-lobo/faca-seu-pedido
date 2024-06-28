package com.jlndev.facaseupedido.ui.recover_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processCompletable
import com.jlndev.baseservice.state.ViewState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.facaseupedido.ui.recover_password.usecase.RecoverPasswordUseCase

class RecoverPasswordViewModel(
    private val useCase: RecoverPasswordUseCase,
    private val schedulerProvider: BaseSchedulerProvider
    ) : BaseViewModel() {

    private val _recoverPasswordLive = MutableLiveData<ViewState<Unit>>()
    val recoverPasswordLive: LiveData<ViewState<Unit>>
        get() = _recoverPasswordLive

    fun sendRecoveryCodeForEmail(email: String) {
        useCase.sendRecoveryCodeForEmail(email)
            .processCompletable(schedulerProvider)
            .doOnSubscribe { _recoverPasswordLive.value = ViewState.Loading(true) }
            .doFinally { _recoverPasswordLive.value = ViewState.Loading(false) }
            .doOnComplete { _recoverPasswordLive.value = ViewState.Success(Unit) }
            .doOnError { _recoverPasswordLive.value = ViewState.Error(it) }
            .disposedBy(disposables)
    }
}