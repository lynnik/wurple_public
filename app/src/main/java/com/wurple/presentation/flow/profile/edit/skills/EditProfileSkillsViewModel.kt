package com.wurple.presentation.flow.profile.edit.skills

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.UserSkill
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class EditProfileSkillsViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val skillsLiveData = MutableLiveData<List<UserSkill>>()

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateSkillsAdd() {
        navigateForwardTo(Screen.EditProfileSkillsAdd())
    }

    fun navigateEditUserSkills(id: String) {
        navigateForwardTo(Screen.EditProfileSkillsAdd(id))
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    skillsLiveData.value = user.getSortedUserSkills()
                }
            }
        )
    }
}