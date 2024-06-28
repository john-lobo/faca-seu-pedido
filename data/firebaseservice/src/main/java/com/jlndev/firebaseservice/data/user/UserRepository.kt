package com.jlndev.firebaseservice.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.firebaseservice.model.User
import io.reactivex.Single
import org.koin.android.ext.android.inject

interface UserRepository{

    fun getUser() : Single<User>

    fun changeUsername(newUsername: String) : Single<User>

    fun changePassword(newPassword: String, lastPassword: String): Single<Unit>

    fun logout(): Single<Unit>
}