package com.wurple.presentation.flow.profile.edit.email.createeditprofileemailrequest

import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.auth.RequestEditProfileEmailUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class CreateEditProfileEmailRequestViewModel(
    private val requestEditProfileEmailUseCase: RequestEditProfileEmailUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    fun navigateBack() {
        navigateBackward()
    }

    fun createRequest() {
        requestEditProfileEmailUseCase.launch(
            param = RequestEditProfileEmailUseCase.Params(),
            block = {
                onComplete = {
                    navigateForwardToAndCloseCurrent(Screen.EditProfileEmailEmailVerification)
                }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }
}