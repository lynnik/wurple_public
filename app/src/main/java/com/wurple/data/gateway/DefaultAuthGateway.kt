package com.wurple.data.gateway

import com.wurple.data.remote.auth.AuthRemoteDataSource
import com.wurple.data.storage.auth.AuthStorageDataSource
import com.wurple.domain.gateway.AuthGateway

class DefaultAuthGateway(
    private val authStorageDataSource: AuthStorageDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthGateway {
    override suspend fun isAuthenticated(): Boolean = authStorageDataSource.isAuthenticated()

    override suspend fun requestSignIn(email: String) {
        authStorageDataSource.setSignInEmail(email)
        authRemoteDataSource.requestSignIn(email)
    }

    override suspend fun requestDeleteAccount(email: String) {
        authRemoteDataSource.requestDeleteAccount(email)
    }

    override suspend fun requestEditProfileEmail(email: String) {
        authRemoteDataSource.requestEditProfileEmail(email)
    }

    override suspend fun isEmailLinkValid(emailLink: String?): Boolean {
        return authRemoteDataSource.isEmailLinkValid(emailLink)
    }

    override suspend fun getEmailLinkDeepLink(emailLink: String?): String? {
        return authRemoteDataSource.getEmailLinkDeepLink(emailLink)
    }

    override suspend fun getReferrerId(): String = authStorageDataSource.getReferrerId()

    override suspend fun setReferrerId(referrerId: String) {
        authStorageDataSource.setReferrerId(referrerId)
    }

    override suspend fun signIn(emailLink: String?) {
        val signInEmail = authStorageDataSource.getSignInEmail()
        authRemoteDataSource.signIn(signInEmail, emailLink)
    }

    override suspend fun reSignIn(email: String, emailLink: String?) {
        authRemoteDataSource.reSignIn(email, emailLink)
    }

    override suspend fun signOut() {
        authRemoteDataSource.signOut()
    }

    override suspend fun clearCache() {
        authStorageDataSource.clearCache()
    }
}
