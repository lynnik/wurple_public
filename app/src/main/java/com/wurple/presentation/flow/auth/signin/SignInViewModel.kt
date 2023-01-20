package com.wurple.presentation.flow.auth.signin

import androidx.lifecycle.MutableLiveData
import com.wurple.data.Wurple
import com.wurple.domain.deeplink.DeepLinkResult
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.auth.RequestSignInUseCase
import com.wurple.domain.usecase.deeplink.HandleDeepLinkUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class SignInViewModel(
    private val deepLink: String,
    private val wurple: Wurple,
    private val handleDeepLinkUseCase: HandleDeepLinkUseCase,
    private val requestSignInUseCase: RequestSignInUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val isActionButtonEnabledLiveData = MutableLiveData(false)
    val privacyPolicyLiveData = SingleLiveEvent<String>()
    val emailErrorMessageLiveData = SingleLiveEvent<String?>()

    init {
        handleDeepLink()
    }

    fun onAgreementsChecked(isChecked: Boolean) {
        isActionButtonEnabledLiveData.value = isChecked
    }

    fun navigateSupport() {
        navigateForwardTo(Screen.Support)
    }

    fun navigatePrivacyPolicy() {
        privacyPolicyLiveData.value = wurple.privacyPolicyUrl
    }

    fun requestSignIn(email: String) {
        clearErrors()
        requestSignInUseCase.launch(
            param = RequestSignInUseCase.Params(email),
            block = {
                onComplete = { navigateForwardTo(Screen.SignInEmailVerification(email)) }
                onError = {
                    handleError(it)
                    (it as? InputValidationException)
                        ?.let { inputValidationException ->
                            inputValidationException.errors.forEach { error ->
                                (error.message as? ValidationResultErrorMessage.Email)
                                    ?.let { emailError ->
                                        emailErrorMessageLiveData.value = emailError.message
                                    }
                            }
                        }
                        ?: showError(it.message)
                }
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    private fun handleDeepLink() {
        handleDeepLinkUseCase.launch(
            param = HandleDeepLinkUseCase.Params(deepLink),
            block = {
                onComplete = { deepLinkResult ->
                    if (deepLinkResult is DeepLinkResult.Preview) {
                        navigateForwardTo(Screen.Preview(deepLinkResult.previewId))
                    }
                }
                onError = ::handleError
            }
        )
    }

    private fun clearErrors() {
        emailErrorMessageLiveData.value = null
    }
}