package com.wurple.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseManager {
    val currentUser: FirebaseUser
        get() = Firebase.auth.currentUser ?: throw IllegalStateException("Current user is null")

    val currentUserEmail: String
        get() = currentUser.email ?: throw IllegalStateException("Current user email is null")

    val isCurrentUserSignedIn: Boolean
        get() = Firebase.auth.currentUser != null
}