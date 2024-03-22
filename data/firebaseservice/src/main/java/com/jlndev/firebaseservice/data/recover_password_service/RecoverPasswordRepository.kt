package com.jlndev.firebaseservice.data.recover_password_service

import com.jlndev.firebaseservice.data.recover_password_service.RecoverPasswordDataSource
import io.reactivex.Completable

class RecoverPasswordRepository(private val dataSource: RecoverPasswordDataSource) {
    fun sendRecoveryCodeForEmail(email: String): Completable {
        return dataSource.sendRecoveryCodeForEmail(email)
    }
}