package com.wurple.presentation.flow.profile.edit.bio

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

class EditProfileBioViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val bioLiveData = MutableLiveData<String>()
    val isNewBioInputLiveData = MutableLiveData(false)
    val bioErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun checkIsNewBioInput(newBio: String) {
        val oldBio = user?.userBio
        isNewBioInputLiveData.value = newBio != oldBio
    }

    fun updateBio(newBio: String) {
        if (isNewBioInputLiveData.value != true) {
            return
        }
        clearErrors()
        val userUpdateRequest = UserUpdateRequest(bio = newBio)
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
                                    is ValidationResultErrorMessage.Bio -> {
                                        bioErrorMessageLiveData.value = error.message.message
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
        bioErrorMessageLiveData.value = null
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileBioViewModel.user = user
                    bioLiveData.value = user.userBio
                }
            }
        )
    }
}