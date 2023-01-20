package com.wurple.presentation.flow.profile.edit.dob

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileDobViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val dateManager: DateManager,
    logger: Logger
) : BaseViewModel(logger) {
    val dobLiveData = MutableLiveData<String>()
    val isNewDobInputLiveData = MutableLiveData(false)
    val dobErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun checkIsNewDobInput(newDob: String) {
        val oldDob = user?.userDob?.date?.baseDate
            ?.let { dateManager.baseDateToFormattedText(it) }
            ?: String.empty
        isNewDobInputLiveData.value = newDob != oldDob
    }

    fun updateDob(newDob: String) {
        if (isNewDobInputLiveData.value != true) {
            return
        }
        clearErrors()
        val userUpdateRequest = UserUpdateRequest(dob = newDob)
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
                                    is ValidationResultErrorMessage.Dob -> {
                                        dobErrorMessageLiveData.value = error.message.message
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
        dobErrorMessageLiveData.value = null
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileDobViewModel.user = user
                    dobLiveData.value = user.userDob?.date?.baseDate
                        ?.let { dateManager.baseDateToFormattedText(it) }
                        ?: String.empty
                }
            }
        )
    }
}