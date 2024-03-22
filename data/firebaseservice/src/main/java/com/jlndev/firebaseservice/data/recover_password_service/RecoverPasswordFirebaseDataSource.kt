package com.jlndev.firebaseservice.data.recover_password_service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jlndev.firebaseservice.model.RecoverPasswordError
import io.reactivex.Completable

class RecoverPasswordFirebaseDataSource(private val firebaseAuth: FirebaseAuth
) : RecoverPasswordDataSource {
    override fun sendRecoveryCodeForEmail(email: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onComplete()
                    }
                }
                .addOnFailureListener {
                    when (it) {
                        is FirebaseAuthInvalidUserException -> {
                            emitter.tryOnError(RecoverPasswordError.EmailNotFound)
                        }
                        else -> {
                            emitter.tryOnError(RecoverPasswordError.GenericError)
                        }
                    }
                }
        }
    }
}