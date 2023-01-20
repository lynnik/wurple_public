package com.wurple.presentation.flow.profile.edit.education

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserEducation
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class EditProfileEducationViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val educationLiveData = MutableLiveData<List<UserEducation>>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateEditUserEducation(id: String) {
        navigateForwardTo(Screen.EditProfileEducationAdd(id))
    }

    fun navigateEducationAdd() {
        navigateForwardTo(Screen.EditProfileEducationAdd())
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileEducationViewModel.user = user
                    educationLiveData.value = user.getSortedUserEducation()
                }
            }
        )
    }
}