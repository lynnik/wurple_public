package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.PreviewGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.preview.UserTempPreview
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class GetTempPreviewUseCase(
    private val userGateway: UserGateway,
    private val previewGateway: PreviewGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<UserTempPreview, GetTempPreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): UserTempPreview {
        val user = userGateway.getCurrentUser()
        val tempPreview = previewGateway.getTempPreview()
        return UserTempPreview(user = user, createPreviewRequest = tempPreview)
    }

    class Params
}