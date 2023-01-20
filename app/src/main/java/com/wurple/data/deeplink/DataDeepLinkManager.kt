package com.wurple.data.deeplink

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.wurple.R
import com.wurple.data.Wurple
import com.wurple.domain.AppInfo
import com.wurple.domain.deeplink.DeepLinkManager
import com.wurple.domain.deeplink.DeepLinkResult
import kotlinx.coroutines.tasks.await

class DataDeepLinkManager(
    private val context: Context,
    private val wurple: Wurple,
    private val appInfo: AppInfo
) : DeepLinkManager {
    override suspend fun getAuthDeepLinkDomain(): String {
        return wurple.authDeepLinkDomain
    }

    override suspend fun getSignInDeepLink(): String {
        return "${wurple.deepLinkWebsite}/${wurple.pathSignIn}"
    }

    override suspend fun getDeleteAccountDeepLink(): String {
        return "${wurple.deepLinkWebsite}/${wurple.pathDeleteAccount}"
    }

    override suspend fun getEditProfileEmailDeepLink(): String {
        return "${wurple.deepLinkWebsite}/${wurple.pathEditProfileEmail}"
    }

    override suspend fun getInviteFriendDeepLink(referrerId: String): String? {
        val shortDynamicLink = Firebase.dynamicLinks
            .shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
                link = Uri.parse(
                    "${wurple.website}/${wurple.pathInviteFriend}/$referrerId"
                )
                domainUriPrefix = wurple.deepLinkWebsite
                androidParameters(appInfo.packageName) {
                    /*NOOP*/
                }
                googleAnalyticsParameters {
                    source = "invite-friend-source"
                    medium = "invite-friend-medium"
                    campaign = "invite-friend-campaign"
                }
                socialMetaTagParameters {
                    title = context.getString(
                        R.string.deep_link_invite_friend_title,
                        context.getString(R.string.app_name)
                    )
                    description = context.getString(R.string.about_app_description)
                    imageUrl = Uri.parse(
                        "https://firebasestorage.googleapis.com/v0/b/wurple-app.appspot.com/o/deep-link-image%2Fwurple-deep-link-image-invite-friend_v2.png?alt=media&token=985dc46a-8aa9-428d-96fb-d75fb731df2f"
                    )
                }
            }
            .await()
        val shortLink = shortDynamicLink.shortLink
        return shortLink?.toString()
    }

    override suspend fun getPreviewDeepLink(previewId: String): String? {
        val shortDynamicLink = Firebase.dynamicLinks
            .shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
                link = Uri.parse(
                    "${wurple.website}/${wurple.pathPreview}/$previewId"
                )
                domainUriPrefix = wurple.deepLinkWebsite
                androidParameters(appInfo.packageName) {
                    fallbackUrl = link
                }
                googleAnalyticsParameters {
                    source = "preview-source"
                    medium = "preview-medium"
                    campaign = "preview-campaign"
                }
                socialMetaTagParameters {
                    title = context.getString(
                        R.string.deep_link_preview_title,
                        context.getString(R.string.app_name)
                    )
                    description = context.getString(R.string.about_app_description)
                    imageUrl = Uri.parse(
                        "https://firebasestorage.googleapis.com/v0/b/wurple-app.appspot.com/o/deep-link-image%2Fwurple-deep-link-image-preview_v2.png?alt=media&token=b7e2a612-2c70-49af-8f37-1464429ea762"
                    )
                }
            }
            .await()
        val shortLink = shortDynamicLink.shortLink
        return shortLink?.toString()
    }

    override suspend fun handleDeepLink(deepLink: String?): DeepLinkResult? {
        if (deepLink == null) {
            return null
        }
        val uri = deepLink.toUri()
        return when {
            uri.lastPathSegment == wurple.pathDeleteAccount -> {
                DeepLinkResult.DeleteAccount
            }
            uri.lastPathSegment == wurple.pathEditProfileEmail -> {
                DeepLinkResult.EditProfileEmail
            }
            uri.pathSegments.contains(wurple.pathPreview) -> {
                val previewId = uri.lastPathSegment
                previewId?.let { DeepLinkResult.Preview(it) }
            }
            uri.pathSegments.contains(wurple.pathInviteFriend) -> {
                val referrerId = uri.lastPathSegment
                referrerId?.let { DeepLinkResult.InviteFriend(it) }
            }
            else -> {
                null
            }
        }
    }
}
