package com.jlndev.firebaseservice.data.register_user_service

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.firebaseservice.model.ConfigFirebase.CHILD_USUARIOS
import com.jlndev.firebaseservice.model.RegistrationError
import com.jlndev.firebaseservice.model.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RegisterUserFirebaseDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
    ) : RegisterUserDataSource {

    override fun registerUser(user: User): Single<User> {
        return Single.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.sendEmailVerification()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = authResult.user!!.uid
                            user.id = userId
                            saveUser(user).subscribeOn(Schedulers.io())
                                .subscribe({
                                    emitter.onSuccess(user)
                                }, {
                                    emitter.onError(it)
                                })
                        } else {
                            authResult.user?.delete()?.addOnSuccessListener {
                                emitter.onError(RegistrationError.EmailVerificationFailed)
                            }
                        }

                    } ?: run {
                        authResult.user?.delete()?.addOnSuccessListener {
                            emitter.onError(RegistrationError.UnknownError)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    when (e) {
                        is FirebaseAuthUserCollisionException -> {
                            Log.w(REGISTER_USER, "${e.message}")
                            emitter.onError(RegistrationError.EmailAlreadyExists)
                        }
                        is FirebaseAuthWeakPasswordException -> {
                            Log.w(REGISTER_USER, "${e.message}")
                            emitter.onError(RegistrationError.WeakPassword)
                        }
                        is FirebaseNetworkException -> {
                            Log.w(REGISTER_USER, "${e.message}")
                            emitter.onError(RegistrationError.NetworkError)
                        }
                        else -> {
                            Log.w(REGISTER_USER, "${e.message}")
                            emitter.onError(RegistrationError.UnknownError)
                        }
                    }
                }
        }
    }

    private fun saveUser(user: User): Completable {
        return Completable.create { emitter ->
            firebaseFirestore
                .collection(CHILD_USUARIOS)
                .document(user.id)
                .set(user)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    companion object {
        const val LOGIN_NORMAL = "LOGIN_NORMAL"
        const val LOGIN_GOOGLE = "LOGIN_GOOGLE"
        const val REGISTER_USER = "REGISTER_USER"
    }
}