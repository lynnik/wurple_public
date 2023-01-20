package com.wurple.presentation.flow.profile.edit.languages

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserLanguage
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel

class EditProfileLanguagesViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val languagesLiveData = MutableLiveData<List<UserLanguage>>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateEditUserLanguage(languageId: Int, languageLevelId: Int) {
        navigateForwardTo(Screen.EditProfileLanguagesAdd(languageId, languageLevelId))
    }

    fun navigateLanguagesAdd() {
        navigateForwardTo(Screen.EditProfileLanguagesAdd())
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileLanguagesViewModel.user = user
                    languagesLiveData.value = user.getSortedUserLanguages()
                }
            }
        )
    }
}