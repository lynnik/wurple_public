package com.wurple.domain.usecase.deeplink

import com.wurple.domain.deeplink.DeepLinkManager
import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlin.coroutines.CoroutineContext

class GetInviteFriendDeepLinkUseCase(
    private val userGateway: UserGateway,
    private val deepLinkManager: DeepLinkManager,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<String?, GetInviteFriendDeepLinkUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): String? {
        val currentUser = userGateway.observeCurrentUser().firstOrNull()
        return currentUser?.let { deepLinkManager.getInviteFriendDeepLink(it.id) }
    }

    class Params
}