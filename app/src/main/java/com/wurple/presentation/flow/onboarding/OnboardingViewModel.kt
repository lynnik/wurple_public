package com.wurple.presentation.flow.onboarding

import com.wurple.domain.log.Logger
import com.wurple.domain.usecase.onboarding.SetOnboardingUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class OnboardingViewModel(
    private val setOnboardingUseCase: SetOnboardingUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    init {
        setOnboardingAsShown()
    }

    fun navigateBack() {
        navigateBackward()
    }

    private fun setOnboardingAsShown() {
        setOnboardingUseCase.launch(
            param = SetOnboardingUseCase.Params(),
            block = { onError = ::handleError }
        )
    }
}
