package com.wurple.data.storage.auth

interface AuthStorageDataSource {
    suspend fun isAuthenticated(): Boolean
    suspend fun getSignInEmail(): String
    suspend fun setSignInEmail(email: String)
    suspend fun getReferrerId(): String
    suspend fun setReferrerId(referrerId: String)
    suspend fun clearCache()
}