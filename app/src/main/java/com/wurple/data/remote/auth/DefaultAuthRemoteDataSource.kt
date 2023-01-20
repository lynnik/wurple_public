package com.wurple.data.remote.auth

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wurple.data.firebase.FirebaseManager
import com.wurple.domain.AppInfo
import com.wurple.domain.deeplink.DeepLinkManager
import kotlinx.coroutines.tasks.await

class DefaultAuthRemoteDataSource(
    private val appInfo: AppInfo,
    private val deepLinkManager: DeepLinkManager,
    private val firebaseManager: FirebaseManager
) : AuthRemoteDataSource {
    override suspend fun requestSignIn(email: String) {
        val actionCodeSettings = getActionCodeSettings(deepLinkManager.getSignInDeepLink())
        Firebase.auth
            .sendSignInLinkToEmail(email, actionCodeSettings)
            .await()
    }

    override suspend fun requestDeleteAccount(email: String) {
        val actionCodeSettings = getActionCodeSettings(deepLinkManager.getDeleteAccountDeepLink())
        Firebase.auth
            .sendSignInLinkToEmail(email, actionCodeSettings)
            .await()
    }

    override suspend fun requestEditProfileEmail(email: String) {
        val actionCodeSettings =
            getActionCodeSettings(deepLinkManager.getEditProfileEmailDeepLink())
        Firebase.auth
            .sendSignInLinkToEmail(email, actionCodeSettings)
            .await()
    }

    override suspend fun isEmailLinkValid(emailLink: String?): Boolean {
        return if (emailLink == null) {
            false
        } else {
            Firebase.auth.isSignInWithEmailLink(emailLink)
        }
    }

    override suspend fun getEmailLinkDeepLink(emailLink: String?): String? {
        val emailLinkUri = emailLink?.toUri()
        val linkUri = emailLinkUri?.getQueryParameter(QUERY_PARAM_LINK)?.toUri()
        return linkUri?.getQueryParameter(QUERY_PARAM_CONTINUE_URL)
    }

    override suspend fun signIn(email: String, emailLink: String?) {
        Firebase.auth
            .signInWithEmailLink(email, emailLink.orEmpty())
            .await()
    }

    override suspend fun reSignIn(email: String, emailLink: String?) {
        val authCredential = EmailAuthProvider.getCredentialWithLink(email, emailLink.orEmpty())
        firebaseManager.currentUser.reauthenticateAndRetrieveData(authCredential).await()
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    private suspend fun getActionCodeSettings(url: String): ActionCodeSettings {
        return actionCodeSettings {
            this.dynamicLinkDomain = deepLinkManager.getAuthDeepLinkDomain()
            this.url = url
            handleCodeInApp = true
            setAndroidPackageName(appInfo.packageName, true, null)
        }
    }

    companion object {
        private const val QUERY_PARAM_LINK = "link"
        private const val QUERY_PARAM_CONTINUE_URL = "continueUrl"
    }
}
