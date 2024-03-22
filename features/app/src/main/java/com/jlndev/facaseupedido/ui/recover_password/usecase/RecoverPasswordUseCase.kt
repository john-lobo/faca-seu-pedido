package com.jlndev.facaseupedido.ui.recover_password.usecase

import io.reactivex.Completable

interface RecoverPasswordUseCase {
    fun sendRecoveryCodeForEmail(email: String): Completable
}