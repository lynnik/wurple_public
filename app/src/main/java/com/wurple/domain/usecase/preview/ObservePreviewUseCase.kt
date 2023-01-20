package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.PreviewGateway
import com.wurple.domain.model.preview.Preview
import com.wurple.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class ObservePreviewUseCase(
    private val previewGateway: PreviewGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : FlowUseCase<List<Preview>, ObservePreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): Flow<List<Preview>> {
        return previewGateway.observePreviews()
    }

    class Params
}