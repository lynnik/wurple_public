package com.wurple.domain.usecase.auth

import com.wurple.domain.deeplink.DeepLinkManager
import com.wurple.domain.deeplink.DeepLinkResult
import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.extension.empty
import com.wurple.domain.gateway.AuthGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class SignInUseCase(
    private val clearCacheUseCase: ClearCacheUseCase,
    private val deepLinkManager: DeepLinkManager,
    private val authGateway: AuthGateway,
    private val userGateway: UserGateway,
    private val logger: Logger,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Screen, SignInUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): Screen {
        val isAuthenticated = authGateway.isAuthenticated()
        val isEmailLinkValid = authGateway.isEmailLinkValid(params.link)
        val emailLink = if (isEmailLinkValid) {
            params.link
        } else {
            null
        }
        val deepLink = if (isEmailLinkValid) {
            authGateway.getEmailLinkDeepLink(emailLink)
        } else {
            params.deepLink
        }
        val deepLinkResult = deepLinkManager.handleDeepLink(deepLink)
        return when {
            // ReSignIn on delete account or on edit profile email
            isAuthenticated &&
                    isEmailLinkValid &&
                    (deepLinkResult is DeepLinkResult.DeleteAccount ||
                            deepLinkResult is DeepLinkResult.EditProfileEmail) -> {
                val email = userGateway.getCurrentUser().userEmail
                authGateway.reSignIn(email, emailLink)
                Screen.Main(deepLink)
            }
            // open Main screen
            isAuthenticated -> {
                Screen.Main(deepLink)
            }
            // SignIn
            isEmailLinkValid -> {
                authGateway.signIn(emailLink)
                // we should try clear cache just after SignIn
                try {
                    clearCacheUseCase.executeOnBackground(ClearCacheUseCase.Params())
                } catch (throwable: Throwable) {
                    logger.e(TAG, throwable)
                }
                if (userGateway.isCurrentUserExist().not()) {
                    val referrerId = authGateway.getReferrerId()
                    userGateway.createUser(referrerId)
                    authGateway.setReferrerId(String.empty)
                }
                Screen.Main()
            }
            // open SignIn screen
            else -> {
                val inviteFriendDeepLinkResult = deepLinkResult as? DeepLinkResult.InviteFriend
                authGateway.setReferrerId(inviteFriendDeepLinkResult?.referrerId.orEmpty())
                Screen.SignIn(deepLink)
            }
        }
    }

    class Params(
        val link: String?,
        val deepLink: String?
    )

    companion object {
        private const val TAG = "SignInUseCase"
    }
}
