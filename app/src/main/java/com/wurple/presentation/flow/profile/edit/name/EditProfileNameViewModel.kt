package com.wurple.presentation.flow.profile.edit.name

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserName
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileNameViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val userNameLiveData = MutableLiveData<UserName>()
    val isNewNameInputLiveData = MutableLiveData(false)
    val firstNameErrorMessageLiveData = SingleLiveEvent<String?>()
    val lastNameErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun checkIsNewNameInput(newFirstName: String, newLastName: String) {
        val oldFirstName = user?.userName?.firstName
        val oldLastName = user?.userName?.lastName
        isNewNameInputLiveData.value = newFirstName != oldFirstName || newLastName != oldLastName
    }

    fun updateName(newFirstName: String, newLastName: String) {
        if (isNewNameInputLiveData.value != true) {
            return
        }
        clearErrors()
        val userUpdateRequest = UserUpdateRequest(
            firstName = newFirstName,
            lastName = newLastName
        )
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
                                    is ValidationResultErrorMessage.FirstName -> {
                                        firstNameErrorMessageLiveData.value = error.message.message
                                    }
                                    is ValidationResultErrorMessage.LastName -> {
                                        lastNameErrorMessageLiveData.value = error.message.message
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
        firstNameErrorMessageLiveData.value = null
        lastNameErrorMessageLiveData.value = null
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileNameViewModel.user = user
                    user.userName?.let { userNameLiveData.value = it }
                }
            }
        )
    }
}