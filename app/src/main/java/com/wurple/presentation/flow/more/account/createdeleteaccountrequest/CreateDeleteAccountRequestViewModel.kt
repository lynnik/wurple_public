package com.wurple.presentation.flow.more.account.createdeleteaccountrequest

import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.auth.RequestDeleteAccountUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class CreateDeleteAccountRequestViewModel(
    private val requestDeleteAccountUseCase: RequestDeleteAccountUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    fun navigateBack() {
        navigateBackward()
    }

    fun createRequest() {
        requestDeleteAccountUseCase.launch(
            param = RequestDeleteAccountUseCase.Params(),
            block = {
                onComplete = {
                    navigateForwardToAndCloseCurrent(Screen.DeleteAccountEmailVerification)
                }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }
}