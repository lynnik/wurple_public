package com.wurple.presentation.flow.profile.edit.email

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserEmailUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileEmailViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserEmailUseCase: UpdateCurrentUserEmailUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val emailLiveData = MutableLiveData<String>()
    val isNewEmailInputLiveData = MutableLiveData(false)
    val emailErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun updateEmail(newEmail: String) {
        if (isNewEmailInputLiveData.value != true) {
            return
        }
        clearErrors()
        updateCurrentUserEmailUseCase.launch(
            param = UpdateCurrentUserEmailUseCase.Params(newEmail),
            block = {
                onComplete = { navigateBackward() }
                onError = {
                    handleError(it)
                    (it as? InputValidationException)
                        ?.let { inputValidationException ->
                            inputValidationException.errors.forEach { error ->
                                when (error.message) {
                                    is ValidationResultErrorMessage.Email -> {
                                        emailErrorMessageLiveData.value = error.message.message
                                    }
                                    else -> {
                                        /*NOOP*/
                                    }
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

    fun checkIsNewEmailInput(newEmail: String) {
        val oldEmail = user?.userEmail
        isNewEmailInputLiveData.value = newEmail != oldEmail
    }

    private fun clearErrors() {
        emailErrorMessageLiveData.value = null
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileEmailViewModel.user = user
                    emailLiveData.value = user.userEmail
                }
            }
        )
    }
}