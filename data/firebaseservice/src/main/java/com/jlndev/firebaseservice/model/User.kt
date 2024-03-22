package com.jlndev.firebaseservice.model

import com.google.firebase.firestore.Exclude

data class User(
    var id: String = "",
    var name: String = "",
    var email:String = "",
    @set:Exclude
    @get:Exclude
    var password:String = ""
)