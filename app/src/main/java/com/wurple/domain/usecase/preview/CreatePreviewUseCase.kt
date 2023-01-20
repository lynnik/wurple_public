package com.wurple.domain.usecase.preview

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.PreviewGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import kotlin.coroutines.CoroutineContext

class CreatePreviewUseCase(
    private val previewTitleInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    private val userGateway: UserGateway,
    private val previewGateway: PreviewGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, CreatePreviewUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        inputValidator.validate(
            previewTitleInputValidationRule.validate(params.createPreviewRequest.title)
        )
        val currentUserId = userGateway.getCurrentUserId()
        previewGateway.createPreview(currentUserId, params.createPreviewRequest)
    }

    class Params(val createPreviewRequest: CreatePreviewRequest)
}
