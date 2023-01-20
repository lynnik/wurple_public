package com.wurple.presentation.flow.profile.edit.experience

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserExperience
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class EditProfileExperienceViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val experienceLiveData = MutableLiveData<List<UserExperience>>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateEditUserExperience(id: String) {
        navigateForwardTo(Screen.EditProfileExperienceAdd(id))
    }

    fun navigateExperienceAdd() {
        navigateForwardTo(Screen.EditProfileExperienceAdd())
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileExperienceViewModel.user = user
                    experienceLiveData.value = user.getSortedUserExperience()
                }
            }
        )
    }
}