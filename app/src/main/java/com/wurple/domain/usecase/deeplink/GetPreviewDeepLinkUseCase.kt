package com.wurple.domain.usecase.deeplink

import com.wurple.domain.deeplink.DeepLinkManager
import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class GetPreviewDeepLinkUseCase(
    private val deepLinkManager: DeepLinkManager,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<String?, GetPreviewDeepLinkUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): String? {
        return deepLinkManager.getPreviewDeepLink(params.previewId)
    }

    class Params(val previewId: String)
}