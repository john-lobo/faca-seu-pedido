package com.jlndev.firebaseservice.data.recover_password_service

import io.reactivex.Completable

interface RecoverPasswordDataSource {
    fun sendRecoveryCodeForEmail(email: String): Completable
}