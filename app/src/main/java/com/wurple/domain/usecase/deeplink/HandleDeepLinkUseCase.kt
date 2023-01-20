package com.wurple.domain.usecase.deeplink

import com.wurple.domain.deeplink.DeepLinkManager
import com.wurple.domain.deeplink.DeepLinkResult
import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class HandleDeepLinkUseCase(
    private val deepLinkManager: DeepLinkManager,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<DeepLinkResult?, HandleDeepLinkUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): DeepLinkResult? {
        return deepLinkManager.handleDeepLink(params.deepLink)
    }

    class Params(val deepLink: String?)
}