package com.wurple.domain.gateway

interface AuthGateway {
    suspend fun isAuthenticated(): Boolean
    suspend fun requestSignIn(email: String)
    suspend fun requestDeleteAccount(email: String)
    suspend fun requestEditProfileEmail(email: String)
    suspend fun isEmailLinkValid(emailLink: String?): Boolean
    suspend fun getEmailLinkDeepLink(emailLink: String?): String?
    suspend fun getReferrerId(): String
    suspend fun setReferrerId(referrerId: String)
    suspend fun signIn(emailLink: String?)
    suspend fun reSignIn(email: String, emailLink: String?)
    suspend fun signOut()
    suspend fun clearCache()
}
