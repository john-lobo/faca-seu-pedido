package com.jlndev.facaseupedido.ui.recover_password.usecase

import com.jlndev.firebaseservice.data.recover_password_service.RecoverPasswordRepository
import io.reactivex.Completable

class RecoverPasswordUseCaseImpl(
    private val repository: RecoverPasswordRepository
): RecoverPasswordUseCase {
    override fun sendRecoveryCodeForEmail(email: String): Completable {
        return repository.sendRecoveryCodeForEmail(email)
    }
}