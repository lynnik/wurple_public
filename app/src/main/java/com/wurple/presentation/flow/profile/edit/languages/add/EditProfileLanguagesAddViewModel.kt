package com.wurple.presentation.flow.profile.edit.languages.add

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserLanguage
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.language.GetLanguageLevelsUseCase
import com.wurple.domain.usecase.language.GetLanguagesUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileLanguagesAddViewModel(
    private val languageId: Int,
    private val languageLevelId: Int,
    private val getLanguagesUseCase: GetLanguagesUseCase,
    private val getLanguageLevelsUseCase: GetLanguageLevelsUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val titleLiveData = MutableLiveData<Title>()
    val languagesLiveData = MutableLiveData<List<Language>>()
    val languageLevelsLiveData = MutableLiveData<List<LanguageLevel>>()
    val selectedLanguageLiveData = MutableLiveData<Language>()
    val selectedLanguageLevelLiveData = MutableLiveData<LanguageLevel>()
    val isDeleteButtonVisibleLiveData = MutableLiveData<Boolean>()
    val alreadyAddedLanguageErrorMessageLiveData = SingleLiveEvent<Unit>()
    val isNewUserLanguageInputLiveData = MutableLiveData(false)
    private var user: User? = null

    init {
        getLanguages()
        getLanguageLevels()
        observeUser()
        titleLiveData.value = if (isEditMode()) Title.Edit else Title.Add
        isDeleteButtonVisibleLiveData.value = isEditMode()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun selectLanguage(language: Language) {
        selectedLanguageLiveData.value = language
        checkIsNewUserLanguageInput()
    }

    fun selectLanguageLevel(languageLevel: LanguageLevel) {
        selectedLanguageLevelLiveData.value = languageLevel
        checkIsNewUserLanguageInput()
    }

    fun addUserLanguage() {
        val newLanguage = selectedLanguageLiveData.value
        val newLanguageLevel = selectedLanguageLevelLiveData.value
        if (newLanguage != null && newLanguageLevel != null) {
            val newUserLanguage = UserLanguage(
                language = newLanguage,
                languageLevel = newLanguageLevel
            )
            val currentUserLanguages =
                user?.getSortedUserLanguages()?.toMutableList() ?: mutableListOf()
            val isNewUserLanguage = currentUserLanguages.contains(newUserLanguage).not()
            if (isNewUserLanguage) {
                // delete the old language if it exists before add the new one,
                // this is available when the user edit a language
                currentUserLanguages
                    .find { it.language.id == languageId && it.languageLevel.id == languageLevelId }
                    ?.let { userLanguageToDelete -> currentUserLanguages.remove(userLanguageToDelete) }
                currentUserLanguages.add(newUserLanguage)
                updateUserLanguages(currentUserLanguages)
            } else {
                alreadyAddedLanguageErrorMessageLiveData.call()
            }
        }
    }

    fun deleteUserLanguage() {
        val currentUserLanguages = user?.getSortedUserLanguages()?.toMutableList()
        currentUserLanguages
            ?.find { it.language.id == languageId && it.languageLevel.id == languageLevelId }
            ?.let { userLanguageToDelete ->
                currentUserLanguages.remove(userLanguageToDelete)
                updateUserLanguages(currentUserLanguages)
            }
    }

    private fun checkIsNewUserLanguageInput() {
        isNewUserLanguageInputLiveData.value = if (isEditMode()) {
            selectedLanguageLiveData.value?.id != languageId ||
                    selectedLanguageLevelLiveData.value?.id != languageLevelId
        } else {
            true
        }
    }

    private fun updateUserLanguages(newUserLanguages: List<UserLanguage>) {
        val userUpdateRequest = UserUpdateRequest(languages = newUserLanguages)
        updateCurrentUserUseCase.launch(
            param = UpdateCurrentUserUseCase.Params(userUpdateRequest),
            block = {
                onComplete = { navigateBack() }
                onError = {
                    handleError(it)
                    showError(it.message)
                }
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    private fun getLanguages() {
        getLanguagesUseCase.launch(
            param = GetLanguagesUseCase.Params(),
            block = {
                onComplete = { languages ->
                    languagesLiveData.value = languages
                    selectedLanguageLiveData.value =
                        languages.find { it.id == languageId } ?: languages.first()
                    checkIsNewUserLanguageInput()
                }
            }
        )
    }

    private fun getLanguageLevels() {
        getLanguageLevelsUseCase.launch(
            param = GetLanguageLevelsUseCase.Params(),
            block = {
                onComplete = { languageLevels ->
                    languageLevelsLiveData.value = languageLevels
                    selectedLanguageLevelLiveData.value =
                        languageLevels.find { it.id == languageLevelId } ?: languageLevels.first()
                    checkIsNewUserLanguageInput()
                }
            }
        )
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileLanguagesAddViewModel.user = user
                }
            }
        )
    }

    private fun isEditMode(): Boolean {
        return languageId != LANGUAGE_DEFAULT_VALUE && languageLevelId != LANGUAGE_DEFAULT_VALUE
    }

    sealed class Title {
        object Add : Title()
        object Edit : Title()
    }

    companion object {
        const val LANGUAGE_DEFAULT_VALUE = -1
    }
}