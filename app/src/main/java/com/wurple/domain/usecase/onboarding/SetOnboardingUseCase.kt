package com.wurple.domain.usecase.onboarding

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.OnboardingGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class SetOnboardingUseCase(
    private val onboardingGateway: OnboardingGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, SetOnboardingUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        onboardingGateway.setOnboardingAsShown()
    }

    class Params
}
