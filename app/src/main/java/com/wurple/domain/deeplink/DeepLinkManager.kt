package com.wurple.domain.deeplink

interface DeepLinkManager {
    suspend fun getAuthDeepLinkDomain(): String
    suspend fun getSignInDeepLink(): String
    suspend fun getDeleteAccountDeepLink(): String
    suspend fun getEditProfileEmailDeepLink(): String
    suspend fun getInviteFriendDeepLink(referrerId: String): String?
    suspend fun getPreviewDeepLink(previewId: String): String?
    suspend fun handleDeepLink(deepLink: String?): DeepLinkResult?
}