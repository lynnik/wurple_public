package com.wurple.presentation.flow.profile.edit.experience.add

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserExperience
import com.wurple.domain.model.user.UserExperienceUpdateRequest
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserExperienceUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import java.util.*

class EditProfileExperienceAddViewModel(
    private val experienceId: String,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserExperienceUseCase: UpdateCurrentUserExperienceUseCase,
    private val dateManager: DateManager,
    logger: Logger
) : BaseViewModel(logger) {
    val titleLiveData = MutableLiveData<Title>()
    val userExperienceLiveData = MutableLiveData<UserExperience>()
    val isDeleteButtonVisibleLiveData = MutableLiveData<Boolean>()
    val isNewExperienceInputLiveData = MutableLiveData(false)
    val positionErrorMessageLiveData = SingleLiveEvent<String?>()
    val companyNameErrorMessageLiveData = SingleLiveEvent<String?>()
    val fromDateErrorMessageLiveData = SingleLiveEvent<String?>()
    val toDateErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
        titleLiveData.value = if (isEditMode()) Title.Edit else Title.Add
        isDeleteButtonVisibleLiveData.value = isEditMode()
        if (isEditMode().not()) {
            isNewExperienceInputLiveData.value = true
        }
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun checkIsNewExperienceInput(
        newPosition: String,
        newCompanyName: String,
        newIsCurrent: Boolean,
        newFromDate: String,
        newToDate: String
    ) {
        val oldExperience = user?.userExperience?.find { it.id == experienceId }
        val oldPosition = oldExperience?.position
        val oldCompanyName = oldExperience?.companyName
        val oldIsCurrent = oldExperience?.isCurrent
        val oldFromDate = oldExperience?.fromDate?.baseDate
            ?.let { dateManager.baseDateToFormattedText(it) }
            ?: String.empty
        val oldToDate = oldExperience?.toDate?.baseDate
            ?.let { dateManager.baseDateToFormattedText(it) }
            ?: String.empty
        isNewExperienceInputLiveData.value =
            newPosition != oldPosition ||
                    newCompanyName != oldCompanyName ||
                    newIsCurrent != oldIsCurrent ||
                    newFromDate != oldFromDate ||
                    if (newIsCurrent) false else newToDate != oldToDate
    }

    fun addOrUpdateUserExperience(
        newPosition: String,
        newCompanyName: String,
        newIsCurrent: Boolean,
        newFromDate: String,
        newToDate: String
    ) {
        if (isNewExperienceInputLiveData.value != true) {
            return
        }
        val userExperienceUpdateRequest = UserExperienceUpdateRequest(
            id = if (isEditMode()) experienceId else UUID.randomUUID().toString(),
            position = newPosition,
            companyName = newCompanyName,
            isCurrent = newIsCurrent,
            fromDate = newFromDate,
            // an empty date can not be parsed
            toDate = if (newIsCurrent) newFromDate else newToDate
        )
        updateUserExperience(userExperienceUpdateRequest)
    }

    fun deleteUserExperience() {
        val userExperienceUpdateRequest = UserExperienceUpdateRequest(
            id = experienceId,
            position = String.empty,
            companyName = String.empty,
            isCurrent = false,
            fromDate = String.empty,
            toDate = String.empty,
            isDelete = true
        )
        updateUserExperience(userExperienceUpdateRequest)
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileExperienceAddViewModel.user = user
                    user.userExperience
                        .find { it.id == experienceId }
                        ?.let { userExperienceLiveData.value = it }
                }
            }
        )
    }

    private fun updateUserExperience(userExperienceUpdateRequest: UserExperienceUpdateRequest) {
        clearErrors()
        updateCurrentUserExperienceUseCase.launch(
            param = UpdateCurrentUserExperienceUseCase.Params(userExperienceUpdateRequest),
            block = {
                onComplete = { navigateBack() }
                onError = {
                    handleError(it)
                    (it as? InputValidationException)
                        ?.let { inputValidationException ->
                            inputValidationException.errors.forEach { error ->
                                when (error.message) {
                                    is ValidationResultErrorMessage.Position -> {
                                        positionErrorMessageLiveData.value = error.message.message
                                    }
                                    is ValidationResultErrorMessage.CompanyName -> {
                                        companyNameErrorMessageLiveData.value =
                                            error.message.message
                                    }
                                    is ValidationResultErrorMessage.PositionDateFrom -> {
                                        fromDateErrorMessageLiveData.value = error.message.message
                                    }
                                    is ValidationResultErrorMessage.PositionDateTo -> {
                                        toDateErrorMessageLiveData.value = error.message.message
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

    private fun isEditMode(): Boolean = experienceId.isNotEmpty()

    private fun clearErrors() {
        positionErrorMessageLiveData.value = null
        companyNameErrorMessageLiveData.value = null
        fromDateErrorMessageLiveData.value = null
        toDateErrorMessageLiveData.value = null
    }

    sealed class Title {
        object Add : Title()
        object Edit : Title()
    }
}