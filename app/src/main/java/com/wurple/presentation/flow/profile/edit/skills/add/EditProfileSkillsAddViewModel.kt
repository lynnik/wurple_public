package com.wurple.presentation.flow.profile.edit.skills.add

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.extension.comma
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserSkill
import com.wurple.domain.model.user.UserSkillsUpdateRequest
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserSkillsUseCase
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import java.util.*

class EditProfileSkillsAddViewModel(
    private val skillId: String,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserSkillsUseCase: UpdateCurrentUserSkillsUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val titleLiveData = MutableLiveData<Title>()
    val isDescVisibleLiveData = MutableLiveData<Boolean>()
    val skillInputLiveData = MutableLiveData<SkillInput>()
    val userSkillLiveData = MutableLiveData<UserSkill>()
    val userSkillTypesLiveData = MutableLiveData<List<UserSkill.Type>>()
    private val selectedSkillLiveData = MutableLiveData<String>()
    val selectedSkillTypeLiveData = MutableLiveData<UserSkill.Type>()
    val isDeleteButtonVisibleLiveData = MutableLiveData<Boolean>()
    val isNewUserSkillInputLiveData = MutableLiveData(false)
    val skillErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null

    init {
        getUserSkillTypes()
        observeUser()
        titleLiveData.value = if (isEditMode()) Title.Edit else Title.Add
        isDescVisibleLiveData.value = isEditMode().not()
        skillInputLiveData.value = if (isEditMode()) SkillInput.Edit else SkillInput.Add
        isDeleteButtonVisibleLiveData.value = isEditMode()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun setSkill(skill: String) {
        selectedSkillLiveData.value = skill
        checkIsNewSkillInput()
    }

    fun selectSkillType(type: UserSkill.Type) {
        selectedSkillTypeLiveData.value = type
        checkIsNewSkillInput()
    }

    fun addOrUpdateUserSkills(
        userSkillsString: String
    ) {
        val newSkillType = selectedSkillTypeLiveData.value
        if (newSkillType != null) {
            val userSkillsUpdateRequest = UserSkillsUpdateRequest(
                userSkills = if (isEditMode()) {
                    listOf(
                        UserSkill(
                            id = skillId,
                            title = userSkillsString,
                            type = newSkillType
                        )
                    )
                } else {
                    userSkillsStringToSet(userSkillsString)
                        .map { newSkill ->
                            UserSkill(
                                id = UUID.randomUUID().toString(),
                                title = newSkill,
                                type = newSkillType
                            )
                        }
                }
            )
            updateUserSkills(userSkillsUpdateRequest)
        }
    }

    fun deleteUserSkill() {
        val userSkillsUpdateRequest = UserSkillsUpdateRequest(
            userSkills = listOf(
                UserSkill(
                    id = skillId,
                    title = String.empty,
                    type = UserSkill.Type.Personal
                )
            ),
            isDelete = true
        )
        updateUserSkills(userSkillsUpdateRequest)
    }

    /**
     * @param userSkillsString "item1,item2,item3, item4 , item5"
     * @return ["item1", "item2", "item3", "item4", "item5"]
     */
    private fun userSkillsStringToSet(userSkillsString: String): Set<String> {
        return if (userSkillsString.contains(String.comma)) {
            userSkillsString
                .split(String.comma)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toSet()
        } else {
            setOf(userSkillsString)
        }
    }

    private fun checkIsNewSkillInput() {
        val newSkill = selectedSkillLiveData.value
        val newSkillType = selectedSkillTypeLiveData.value
        val oldUserSkill = user?.getSortedUserSkills()?.find { it.id == skillId }
        val oldSkill = oldUserSkill?.title
        val oldSkillType = oldUserSkill?.type
        isNewUserSkillInputLiveData.value = if (isEditMode()) {
            newSkill != oldSkill || newSkillType != oldSkillType
        } else {
            true
        }
    }

    private fun updateUserSkills(userSkillsUpdateRequest: UserSkillsUpdateRequest) {
        clearErrors()
        updateCurrentUserSkillsUseCase.launch(
            param = UpdateCurrentUserSkillsUseCase.Params(userSkillsUpdateRequest),
            block = {
                onComplete = { navigateBack() }
                onError = {
                    handleError(it)
                    (it as? InputValidationException)
                        ?.let { inputValidationException ->
                            inputValidationException.errors.forEach { error ->
                                when (error.message) {
                                    is ValidationResultErrorMessage.Skill -> {
                                        skillErrorMessageLiveData.value = error.message.message
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

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileSkillsAddViewModel.user = user
                    user.getSortedUserSkills()
                        .find { it.id == skillId }
                        ?.let {
                            userSkillLiveData.value = it
                            selectedSkillTypeLiveData.value = it.type
                            checkIsNewSkillInput()
                        }
                        ?: run {
                            selectedSkillTypeLiveData.value = getSkillTypes().first()
                            checkIsNewSkillInput()
                        }
                }
            }
        )
    }

    private fun getUserSkillTypes() {
        userSkillTypesLiveData.value = getSkillTypes()
        checkIsNewSkillInput()
    }

    private fun clearErrors() {
        skillErrorMessageLiveData.value = null
    }

    private fun getSkillTypes(): List<UserSkill.Type> {
        return listOf(UserSkill.Type.Personal, UserSkill.Type.Professional)
    }

    private fun isEditMode(): Boolean = skillId.isNotEmpty()

    sealed class Title {
        object Add : Title()
        object Edit : Title()
    }

    sealed class SkillInput {
        object Add : SkillInput()
        object Edit : SkillInput()
    }
}