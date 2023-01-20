package com.wurple.data

import com.wurple.BuildConfig

class Wurple {
    val domain: String
        get() = BuildConfig.DOMAIN
    val authDeepLinkDomain: String
        get() = BuildConfig.SIGN_IN_DOMAIN
    val website: String
        get() = "${BuildConfig.SCHEME}://$domain"
    val deepLinkWebsite: String
        get() = "${website}/${BuildConfig.PATH_DEEP_LINK}"

    val pathSignIn: String
        get() = BuildConfig.PATH_SIGN_IN
    val pathDeleteAccount: String
        get() = BuildConfig.PATH_DELETE_ACCOUNT
    val pathEditProfileEmail: String
        get() = BuildConfig.PATH_EDIT_PROFILE_EMAIL
    val pathPreview: String
        get() = BuildConfig.PATH_PREVIEW
    val pathInviteFriend: String
        get() = BuildConfig.PATH_INVITE_FRIEND

    val privacyPolicyUrl: String
        get() = "$website/privacy-policy"
    val termsAndConditionsUrl: String
        get() = "$website/terms-and-conditions"
    val supportEmail: String
        get() = "support@$domain"
    val supportTelegram: String
        get() = "tg://resolve?domain=wurple_app"
}
