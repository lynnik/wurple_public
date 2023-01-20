package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.PreviewGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.preview.UserPreview
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class GetPreviewUseCase(
    private val userGateway: UserGateway,
    private val previewGateway: PreviewGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<UserPreview?, GetPreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): UserPreview? {
        return if (params.id.isNullOrEmpty()) {
            val user = userGateway.getCurrentUserFromStorage()
            // a preview with default params needed on creation flow
            val preview = previewGateway.getDefaultPreview(user.id)
            UserPreview(user = user, preview = preview)
        } else {
            val preview = previewGateway.getPreviewById(params.id)
            preview?.let {
                val user = userGateway.getUserById(it.userId)
                UserPreview(user = user, preview = it)
            }
        }
    }

    class Params(val id: String?)
}