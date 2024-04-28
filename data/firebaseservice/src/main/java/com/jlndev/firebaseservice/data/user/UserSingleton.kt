package com.jlndev.firebaseservice.data.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.baseservice.firebase.ConfigFirebase.CHILD_USERS
import com.jlndev.firebaseservice.model.LoginError
import com.jlndev.firebaseservice.model.User

object UserSingleton {

    private var user = MutableLiveData<User?>()

    fun getUser(): LiveData<User?> {
        return user
    }

    fun loadUserFromFirebase(fireStore: FirebaseFirestore, auth: FirebaseAuth) {
        auth.uid?.let {
            fireStore
                .collection(CHILD_USERS)
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        user.value = document.toObject(User::class.java)
                    } else {
                        user.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("LOAD_USER", "loadUserFromFirebase: ${exception.message}", exception)
                    LoginError.UserNotFound.message
                    user.value = null
                }
        } ?: run {
            user.value = null
        }
    }
}