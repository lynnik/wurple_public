package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.model.preview.UserPreview
import com.wurple.domain.usecase.base.FlowUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.coroutines.CoroutineContext

class ObserveUserPreviewUseCase(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val observePreviewUseCase: ObservePreviewUseCase,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : FlowUseCase<List<UserPreview>, ObserveUserPreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): Flow<List<UserPreview>> {
        val currentUserFlow =
            observeCurrentUserUseCase.executeOnBackground(ObserveCurrentUserUseCase.Params())
        val previewFlow = observePreviewUseCase.executeOnBackground(ObservePreviewUseCase.Params())
        return currentUserFlow.combine(previewFlow) { user, previews ->
            previews.map { preview -> UserPreview(user, preview) }
        }
    }

    class Params
}