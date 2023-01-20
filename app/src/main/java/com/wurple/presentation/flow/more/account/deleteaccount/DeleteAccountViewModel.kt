package com.wurple.presentation.flow.more.account.deleteaccount

import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.account.DeleteAccountUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class DeleteAccountViewModel(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    logger: Logger
) : BaseViewModel(logger) {

    fun navigateBack() {
        navigateBackward()
    }

    fun deleteAccount() {
        deleteAccountUseCase.launch(
            param = DeleteAccountUseCase.Params(),
            block = {
                onComplete = { navigateForwardToAsRoot(Screen.SignIn()) }
                onError = {
                    handleError(it)
                    showError(it.message)
                }
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }
}