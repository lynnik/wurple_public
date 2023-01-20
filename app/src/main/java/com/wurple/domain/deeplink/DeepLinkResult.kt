package com.wurple.domain.deeplink

sealed class DeepLinkResult {
    object DeleteAccount : DeepLinkResult()
    object EditProfileEmail : DeepLinkResult()
    data class Preview(val previewId: String) : DeepLinkResult()
    data class InviteFriend(val referrerId: String) : DeepLinkResult()
}
