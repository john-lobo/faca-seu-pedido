package com.jlndev.firebaseservice.data.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.baseservice.firebase.ConfigFirebase
import com.jlndev.firebaseservice.model.User
import io.reactivex.Single

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {
    override fun getUser(): Single<User> {
        return Single.create { emitter ->
            firebaseAuth.currentUser?.let { firebaseUser ->
                val userId = firebaseUser.uid
                val userRef = firestore.collection(ConfigFirebase.CHILD_USERS).document(userId)

                userRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val user = documentSnapshot.toObject(User::class.java)
                            if (user != null) {
                                emitter.onSuccess(user)
                            } else {
                                emitter.onError(Exception("User data is null"))
                            }
                        } else {
                            emitter.onError(Exception("User document does not exist"))
                        }
                    }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
            } ?: run {
                emitter.onError(Exception("User is not authenticated"))
            }
        }
    }

    override fun changeUsername(newUsername: String): Single<User> {
        return Single.create { emitter ->
            firebaseAuth.currentUser?.let { firebaseUser ->
                val userId = firebaseUser.uid
                val userRef = firestore.collection(ConfigFirebase.CHILD_USERS).document(userId)

                userRef.update("name", newUsername)
                    .addOnSuccessListener {
                        val updatedUser = User(id = userId, name = newUsername, email = firebaseUser.email ?: "")
                        emitter.onSuccess(updatedUser)
                    }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
            } ?: run {
                emitter.onError(Exception("User is not authenticated"))
            }
        }
    }

    override fun changePassword(newPassword: String, lastPassword: String): Single<Unit> {
        return Single.create { emitter ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val credentials = firebaseAuth.currentUser?.email?.let { EmailAuthProvider.getCredential(it, lastPassword) }
                if (credentials != null) {
                    currentUser.reauthenticate(credentials)
                        .addOnSuccessListener {
                            currentUser.updatePassword(newPassword)
                                .addOnSuccessListener {
                                    emitter.onSuccess(Unit)
                                }
                                .addOnFailureListener { exception ->
                                    emitter.onError(exception)
                                }
                        }
                        .addOnFailureListener { exception ->
                            emitter.onError(exception)
                        }
                } else {
                    emitter.onError(Exception("Invalid credentials"))
                }
            } else {
                emitter.onError(Exception("User is not authenticated"))
            }
        }
    }

    override fun logout(): Single<Unit> {
        return Single.create { emitter ->
            try {
                firebaseAuth.signOut()
                emitter.onSuccess(Unit)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}