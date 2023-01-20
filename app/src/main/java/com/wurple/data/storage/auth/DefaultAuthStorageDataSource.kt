package com.wurple.data.storage.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wurple.data.datastore.AuthDataStore

class DefaultAuthStorageDataSource(
    private val authDataStore: AuthDataStore
) : AuthStorageDataSource {
    override suspend fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null

    override suspend fun getSignInEmail(): String = authDataStore.getSignInEmail()

    override suspend fun setSignInEmail(email: String) {
        authDataStore.setSignInEmail(email)
    }

    override suspend fun getReferrerId(): String = authDataStore.getReferrerId()

    override suspend fun setReferrerId(referrerId: String) {
        authDataStore.setReferrerId(referrerId)
    }

    override suspend fun clearCache() {
        authDataStore.clearDataStore()
    }
}