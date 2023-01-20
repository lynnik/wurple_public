package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.PreviewGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class FetchPreviewUseCase(
    private val userGateway: UserGateway,
    private val previewGateway: PreviewGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, FetchPreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        val currentUserId = userGateway.getCurrentUserId()
        previewGateway.fetchPreviews(currentUserId)
    }

    class Params
}