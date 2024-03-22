package com.jlndev.firebaseservice.data.login_service

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.firebaseservice.model.ConfigFirebase.CHILD_EMAIL
import com.jlndev.firebaseservice.model.ConfigFirebase.CHILD_USUARIOS
import com.jlndev.firebaseservice.model.LoginError
import com.jlndev.firebaseservice.model.LoginWithGoogleError
import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

class LoginFirebaseDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : LoginDataSource {

    override fun login(email: String, password: String): Single<User> {
        return Single.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userId = authResult.user?.uid ?: throw Exception("Failed to get user ID")
                    firebaseFirestore.collection(CHILD_USUARIOS).document(userId).get()
                        .addOnSuccessListener { userSnapshot ->
                            val user = userSnapshot.toObject(User::class.java)
                            if (user != null) {
                                emitter.onSuccess(user)
                            } else {
                                emitter.onError(Exception("Failed to get user data"))
                            }
                        }
                        .addOnFailureListener { e ->
                            when (e) {
                                is FirebaseAuthInvalidUserException -> emitter.onError(LoginError.UserNotFound)
                                is FirebaseAuthInvalidCredentialsException -> emitter.onError(
                                    LoginError.InvalidCredentials)
                                is FirebaseNetworkException -> emitter.onError(LoginError.NetworkError)
                                else -> {
                                    Log.e(LOGIN_NORMAL, e.message ?: "")
                                    emitter.onError(LoginError.UnknownError)
                                }
                            }
                        }
                }
                .addOnFailureListener { e ->
                    when (e) {
                        is FirebaseAuthInvalidUserException -> emitter.onError(LoginError.UserNotFound)
                        is FirebaseAuthInvalidCredentialsException -> emitter.onError(LoginError.InvalidCredentials)
                        is FirebaseNetworkException -> emitter.onError(LoginError.NetworkError)
                        else -> {
                            Log.e(LOGIN_NORMAL, e.message ?: "")
                            emitter.onError(LoginError.UnknownError)
                        }
                    }
                }
        }
    }

    override fun loginWithGoogle(idToken: String): Single<User> {
        return Single.create { emitter ->
            try {
                val provider = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(provider)
                    .addOnSuccessListener { authResult ->
                        val userGoogle = authResult.user
                        val userId = userGoogle?.uid ?: throw Exception("Failed to get user ID")
                        val user = User(userId, userGoogle.displayName ?: "", userGoogle.email ?: "")

                        // Verificar se o usuário já existe no Firestore
                        val usersCollection = firebaseFirestore.collection(CHILD_USUARIOS)
                        usersCollection.whereEqualTo(CHILD_EMAIL, user.email).get()
                            .addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    // O usuário já existe
                                    val existingUser = querySnapshot.documents[0].toObject(User::class.java)
                                    existingUser?.let {
                                        emitter.onSuccess(it)
                                    } ?: run {
                                        emitter.onError(Exception("Failed to get existing user"))
                                    }
                                } else {
                                    // O usuário não existe, então salve-o no Firestore
                                    saveUser(user)
                                    emitter.onSuccess(user)
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e(LOGIN_GOOGLE, e.message ?: "", e)
                                emitter.onError(LoginWithGoogleError.UnknownError)
                            }
                    }
                    .addOnFailureListener { e ->
                        when (e) {
                            is ApiException -> {
                                Log.w(LOGIN_GOOGLE, "${e.message} ${e.statusCode}")
                                emitter.onError(LoginWithGoogleError.UnknownError)
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                Log.w(LOGIN_GOOGLE, "${e.message}")
                                emitter.onError(LoginWithGoogleError.InvalidCredentials)
                            }
                            is FirebaseAuthInvalidUserException -> {
                                Log.w(LOGIN_GOOGLE, "${e.message}")
                                emitter.onError(LoginWithGoogleError.UserNotFound)
                            }
                            is FirebaseAuthUserCollisionException -> {
                                Log.w(LOGIN_GOOGLE, "${e.message}")
                                emitter.onError(LoginWithGoogleError.GoogleAccountEmailAlreadyInUseError)
                            }
                            else -> {
                                Log.w(LOGIN_GOOGLE, "${e.message}")
                                emitter.onError(LoginWithGoogleError.UnknownError)
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.w(LOGIN_GOOGLE, "${e.message}")
                emitter.onError(LoginWithGoogleError.UnknownError)
            }
        }
    }

    private fun saveUser(user: User) {
        firebaseFirestore.collection(CHILD_USUARIOS).document(user.id).set(user)
    }

    companion object {
        const val LOGIN_NORMAL = "LOGIN_NORMAL"
        const val LOGIN_GOOGLE = "LOGIN_GOOGLE"
        const val REGISTER_USER = "REGISTER_USER"
    }
}