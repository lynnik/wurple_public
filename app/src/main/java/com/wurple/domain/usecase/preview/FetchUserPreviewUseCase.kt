package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.usecase.user.FetchCurrentUserUseCase
import kotlin.coroutines.CoroutineContext

class FetchUserPreviewUseCase(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val fetchPreviewUseCase: FetchPreviewUseCase,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, FetchUserPreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        fetchCurrentUserUseCase.executeOnBackground(FetchCurrentUserUseCase.Params())
        fetchPreviewUseCase.executeOnBackground(FetchPreviewUseCase.Params())
    }

    class Params
}