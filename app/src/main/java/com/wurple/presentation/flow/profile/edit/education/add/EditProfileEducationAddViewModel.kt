package com.wurple.presentation.flow.profile.edit.education.add

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserEducation
import com.wurple.domain.model.user.UserEducationUpdateRequest
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserEducationUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import java.util.*

class EditProfileEducationAddViewModel(
    private val educationId: String,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserEducationUseCase: UpdateCurrentUserEducationUseCase,
    private val dateManager: DateManager,
    logger: Logger
) : BaseViewModel(logger) {
    val titleLiveData = MutableLiveData<Title>()
    val userEducationLiveData = MutableLiveData<UserEducation>()
    val isDeleteButtonVisibleLiveData = MutableLiveData<Boolean>()
    val isNewEducationInputLiveData = MutableLiveData(false)
    val degreeErrorMessageLiveData = SingleLiveEvent<String?>()
    val institutionErrorMessageLiveData = SingleLiveEvent<String?>()
    val graduationDateErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        observeUser()
        titleLiveData.value = if (isEditMode()) Title.Edit else Title.Add
        isDeleteButtonVisibleLiveData.value = isEditMode()
        if (isEditMode().not()) {
            isNewEducationInputLiveData.value = true
        }
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun checkIsNewEducationInput(
        newDegree: String,
        newInstitution: String,
        newGraduationDate: String
    ) {
        val oldEducation = user?.userEducation?.find { it.id == educationId }
        val oldDegree = oldEducation?.degree
        val oldInstitution = oldEducation?.institution
        val oldGraduationDate = oldEducation?.graduationDate?.baseDate
            ?.let { dateManager.baseDateToFormattedText(it) }
            ?: String.empty
        isNewEducationInputLiveData.value =
            newDegree != oldDegree ||
                    newInstitution != oldInstitution ||
                    newGraduationDate != oldGraduationDate
    }

    fun addOrUpdateUserEducation(
        newDegree: String,
        newInstitution: String,
        newGraduationDate: String
    ) {
        if (isNewEducationInputLiveData.value != true) {
            return
        }
        val userEducationUpdateRequest = UserEducationUpdateRequest(
            id = if (isEditMode()) educationId else UUID.randomUUID().toString(),
            degree = newDegree,
            institution = newInstitution,
            graduationDate = newGraduationDate
        )
        updateUserEducation(userEducationUpdateRequest)
    }

    fun deleteUserEducation() {
        val userEducationUpdateRequest = UserEducationUpdateRequest(
            id = educationId,
            degree = String.empty,
            institution = String.empty,
            graduationDate = String.empty,
            isDelete = true
        )
        updateUserEducation(userEducationUpdateRequest)
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileEducationAddViewModel.user = user
                    user.userEducation
                        .find { it.id == educationId }
                        ?.let { userEducationLiveData.value = it }
                }
            }
        )
    }

    private fun updateUserEducation(userEducationUpdateRequest: UserEducationUpdateRequest) {
        clearErrors()
        updateCurrentUserEducationUseCase.launch(
            param = UpdateCurrentUserEducationUseCase.Params(userEducationUpdateRequest),
            block = {
                onComplete = { navigateBack() }
                onError = {
                    handleError(it)
                    (it as? InputValidationException)
                        ?.let { inputValidationException ->
                            inputValidationException.errors.forEach { error ->
                                when (error.message) {
                                    is ValidationResultErrorMessage.Degree -> {
                                        degreeErrorMessageLiveData.value = error.message.message
                                    }
                                    is ValidationResultErrorMessage.Institution -> {
                                        institutionErrorMessageLiveData.value =
                                            error.message.message
                                    }
                                    is ValidationResultErrorMessage.GraduationDate -> {
                                        graduationDateErrorMessageLiveData.value =
                                            error.message.message
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

    private fun isEditMode(): Boolean = educationId.isNotEmpty()

    private fun clearErrors() {
        degreeErrorMessageLiveData.value = null
        institutionErrorMessageLiveData.value = null
        graduationDateErrorMessageLiveData.value = null
    }

    sealed class Title {
        object Add : Title()
        object Edit : Title()
    }
}