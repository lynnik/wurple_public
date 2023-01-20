package com.wurple.presentation.flow.root

import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.auth.SignInUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class RootViewModel(
    private val signInUseCase: SignInUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val authCheckedLiveData = SingleLiveEvent<Unit>()

    fun checkAuthOrSignIn(link: String?, deepLink: String?) {
        signInUseCase.launch(
            param = SignInUseCase.Params(link, deepLink),
            block = {
                onComplete = { screen ->
                    navigateForwardToAsRoot(screen)
                    authCheckedLiveData.call()
                }
                onError = {
                    handleError(it)
                    navigateForwardToAsRoot(Screen.SignIn())
                    authCheckedLiveData.call()
                }
            }
        )
    }
}