package com.wurple.domain.usecase.language

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.LanguageGateway
import com.wurple.domain.model.language.LanguageLevel
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class GetLanguageLevelsUseCase(
    private val languageGateway: LanguageGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<List<LanguageLevel>, GetLanguageLevelsUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): List<LanguageLevel> {
        return languageGateway.getLanguageLevels().sortedBy { it.id }
    }

    class Params
}