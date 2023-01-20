package com.wurple.data.remote.auth

interface AuthRemoteDataSource {
    suspend fun requestSignIn(email: String)
    suspend fun requestDeleteAccount(email: String)
    suspend fun requestEditProfileEmail(email: String)
    suspend fun isEmailLinkValid(emailLink: String?): Boolean
    suspend fun getEmailLinkDeepLink(emailLink: String?): String?
    suspend fun signIn(email: String, emailLink: String?)
    suspend fun reSignIn(email: String, emailLink: String?)
    suspend fun signOut()
}
