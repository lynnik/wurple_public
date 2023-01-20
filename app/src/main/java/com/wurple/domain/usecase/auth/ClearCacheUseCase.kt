package com.wurple.domain.usecase.auth

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.*
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class ClearCacheUseCase(
    private val authGateway: AuthGateway,
    private val onboardingGateway: OnboardingGateway,
    private val infoGateway: InfoGateway,
    private val userGateway: UserGateway,
    private val previewGateway: PreviewGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, ClearCacheUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        authGateway.clearCache()
        onboardingGateway.clearCache()
        infoGateway.clearCache()
        userGateway.clearCache()
        previewGateway.clearCache()
    }

    class Params
}
