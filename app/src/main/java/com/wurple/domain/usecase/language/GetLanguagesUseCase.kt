package com.wurple.domain.usecase.language

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.LanguageGateway
import com.wurple.domain.model.language.Language
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class GetLanguagesUseCase(
    private val languageGateway: LanguageGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<List<Language>, GetLanguagesUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): List<Language> {
        return languageGateway.getLanguages().sortedBy { it.name }
    }

    class Params
}