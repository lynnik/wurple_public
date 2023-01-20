package com.wurple.presentation.flow.more.account

import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.auth.SignOutUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class AccountViewModel(
    private val signOutUseCase: SignOutUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    fun navigateBack() {
        navigateBackward()
    }

    fun navigateEditProfileEmail() {
        navigateForwardTo(Screen.CreateEditProfileEmailRequest)
    }

    fun navigateCreateDeleteAccountRequest() {
        navigateForwardTo(Screen.CreateDeleteAccountRequest)
    }

    fun signOut() {
        signOutUseCase.launch(
            param = SignOutUseCase.Params(),
            block = {
                onComplete = {
                    navigateForwardToAsRoot(Screen.SignIn())
                }
                onError = {
                    handleError(it)
                    navigateForwardToAsRoot(Screen.SignIn())
                }
                onStart = ::showProgress
            }
        )
    }
}
