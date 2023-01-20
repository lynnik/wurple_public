package com.wurple.presentation.flow.profile.edit.username

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileUsernameViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val usernameLiveData = MutableLiveData<String>()
    val isNewUsernameInputLiveData = MutableLiveData(false)
    val usernameErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun checkIsNewUsernameInput(newUsername: String) {
        val oldUsername = user?.userUsername
        isNewUsernameInputLiveData.value = newUsername != oldUsername
    }

    fun updateUsername(newUsername: String) {
        if (isNewUsernameInputLiveData.value != true) {
            return
        }
        clearErrors()
        val userUpdateRequest = UserUpdateRequest(username = newUsername)
        updateCurrentUserUseCase.launch(
            param = UpdateCurrentUserUseCase.Params(userUpdateRequest),
            block = {
                onComplete = { navigateBack() }
                onError = {
                    handleError(it)
                    (it as? InputValidationException)
                        ?.let { inputValidationException ->
                            inputValidationException.errors.forEach { error ->
                                when (error.message) {
                                    is ValidationResultErrorMessage.Username -> {
                                        usernameErrorMessageLiveData.value = error.message.message
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

    private fun clearErrors() {
        usernameErrorMessageLiveData.value = null
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileUsernameViewModel.user = user
                    usernameLiveData.value = user.userUsername
                }
            }
        )
    }
}