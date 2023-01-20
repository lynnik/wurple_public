package com.wurple.presentation.flow.previews.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.PreviewLifetimeOption
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.domain.model.preview.UserTempPreview
import com.wurple.domain.model.user.User
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.preview.*
import com.wurple.domain.validation.InputValidationException
import com.wurple.domain.validation.ValidationResultErrorMessage
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import com.wurple.presentation.flow.previews.add.dialog.adapter.PreviewOptionItem
import com.wurple.presentation.model.mapper.FromUserTempPreviewToPreviewUiOnAddMapper
import com.wurple.presentation.model.preview.PreviewUi
import kotlinx.coroutines.launch

class PreviewsAddViewModel(
    private val previewId: String,
    private val getPreviewUseCase: GetPreviewUseCase,
    private val createPreviewUseCase: CreatePreviewUseCase,
    private val updatePreviewUseCase: UpdatePreviewUseCase,
    private val deletePreviewUseCase: DeletePreviewUseCase,
    private val setTempPreviewUseCase: SetTempPreviewUseCase,
    private val fromUserTempPreviewToPreviewUiOnAddMapper: FromUserTempPreviewToPreviewUiOnAddMapper,
    private val dispatcherProvider: DispatcherProvider,
    logger: Logger
) : BaseViewModel(logger) {
    val modeLiveData = MutableLiveData<Mode>()
    val isAddButtonEnabledLiveData = MutableLiveData<Boolean>()
    val isDeleteButtonVisibleLiveData = MutableLiveData<Boolean>()
    val previewHeaderLiveData = MutableLiveData<PreviewHeader>()
    val emptyStateLiveData = SingleLiveEvent<EmptyState>()
    val previewUiLiveData = MutableLiveData<PreviewUi>()
    val userImagePreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userNamePreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userPositionPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userLocationPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userEmailPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userUsernamePreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userBioPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userSkillsPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userExperiencePreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userEducationPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val userLanguagesPreviewOptionItemsLiveData = SingleLiveEvent<List<PreviewOptionItem>>()
    val previewTitleErrorMessageLiveData = SingleLiveEvent<String?>()
    private var user: User? = null
    private var createPreviewRequest: CreatePreviewRequest? = null
    private var previousCreatePreviewRequest: CreatePreviewRequest? = null

    init {
        modeLiveData.value = if (isEditMode()) Mode.Edit else Mode.Add
        getPreview()
    }

    override fun showProgress() {
        super.showProgress()
        isAddButtonEnabledLiveData.value = false
    }

    override fun hideProgress() {
        super.hideProgress()
        isAddButtonEnabledLiveData.value = isAddButtonEnabled()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateTempPreview() {
        clearErrors()
        createPreviewRequest?.let { request ->
            setTempPreviewUseCase.launch(
                param = SetTempPreviewUseCase.Params(createPreviewRequest = request),
                block = {
                    onComplete = {
                        navigateForwardTo(Screen.TempPreview)
                    }
                    onError = ::handleCreationError
                    onStart = ::showProgress
                    onTerminate = ::hideProgress
                }
            )
        }
    }

    fun createPreview() {
        clearErrors()
        createPreviewRequest?.let { request ->
            if (isEditMode()) {
                updatePreviewUseCase.launch(
                    param = UpdatePreviewUseCase.Params(previewId, request),
                    block = {
                        onComplete = { navigateBackwardTo(Screen.Main()) }
                        onError = ::handleCreationError
                        onStart = ::showProgress
                        onTerminate = ::hideProgress
                    }
                )
            } else {
                createPreviewUseCase.launch(
                    param = CreatePreviewUseCase.Params(request),
                    block = {
                        onComplete = { navigateBackwardTo(Screen.Main()) }
                        onError = ::handleCreationError
                        onStart = ::showProgress
                        onTerminate = ::hideProgress
                    }
                )
            }
        }
    }

    fun onUserImageItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = String.empty,
                visibleIsChecked = createPreviewRequest?.userImageVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userImageVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userImagePreviewOptionItemsLiveData.value = items
    }

    fun onUserNameItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = user?.userName?.fullName ?: String.empty,
                visibleIsChecked = createPreviewRequest?.userNameVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.PartiallyVisible(
                partiallyVisibleSubtext = user?.userName?.shortFullName ?: String.empty,
                partiallyVisibleIsChecked = createPreviewRequest?.userNameVisibility == PreviewVisibilityValue.PartiallyVisible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userNameVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userNamePreviewOptionItemsLiveData.value = items
    }

    fun onUserPositionItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = user?.getCurrentUserExperience()?.title ?: String.empty,
                visibleIsChecked = createPreviewRequest?.userCurrentPositionVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.PartiallyVisible(
                partiallyVisibleSubtext = user?.getCurrentUserExperience()?.position
                    ?: String.empty,
                partiallyVisibleIsChecked = createPreviewRequest?.userCurrentPositionVisibility == PreviewVisibilityValue.PartiallyVisible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userCurrentPositionVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userPositionPreviewOptionItemsLiveData.value = items
    }

    fun onUserLocationItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = user?.userLocation?.formattedLocation ?: String.empty,
                visibleIsChecked = createPreviewRequest?.userLocationVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.PartiallyVisible(
                partiallyVisibleSubtext = user?.userLocation?.country ?: String.empty,
                partiallyVisibleIsChecked = createPreviewRequest?.userLocationVisibility == PreviewVisibilityValue.PartiallyVisible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userLocationVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userLocationPreviewOptionItemsLiveData.value = items
    }

    fun onUserEmailItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = user?.userEmail ?: String.empty,
                visibleIsChecked = createPreviewRequest?.userEmailVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userEmailVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userEmailPreviewOptionItemsLiveData.value = items
    }

    fun onUserUsernameItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = user?.userUsername ?: String.empty,
                visibleIsChecked = createPreviewRequest?.userUsernameVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userUsernameVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userUsernamePreviewOptionItemsLiveData.value = items
    }

    fun onUserBioItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = user?.userBio ?: String.empty,
                visibleIsChecked = createPreviewRequest?.userBioVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userBioVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userBioPreviewOptionItemsLiveData.value = items
    }

    fun onUserSkillsItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = String.empty,
                visibleIsChecked = createPreviewRequest?.userSkillsVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userSkillsVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userSkillsPreviewOptionItemsLiveData.value = items
    }

    fun onUserExperienceItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = String.empty,
                visibleIsChecked = createPreviewRequest?.userExperienceVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.PartiallyVisible(
                partiallyVisibleSubtext = String.empty,
                partiallyVisibleIsChecked = createPreviewRequest?.userExperienceVisibility == PreviewVisibilityValue.PartiallyVisible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userExperienceVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userExperiencePreviewOptionItemsLiveData.value = items
    }

    fun onUserEducationItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = String.empty,
                visibleIsChecked = createPreviewRequest?.userEducationVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.PartiallyVisible(
                partiallyVisibleSubtext = String.empty,
                partiallyVisibleIsChecked = createPreviewRequest?.userEducationVisibility == PreviewVisibilityValue.PartiallyVisible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userEducationVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userEducationPreviewOptionItemsLiveData.value = items
    }

    fun onUserLanguagesItemClick() {
        val items = listOf(
            PreviewOptionItem.Visible(
                visibleSubtext = String.empty,
                visibleIsChecked = createPreviewRequest?.userLanguagesVisibility == PreviewVisibilityValue.Visible
            ),
            PreviewOptionItem.Hidden(
                hiddenIsChecked = createPreviewRequest?.userLanguagesVisibility == PreviewVisibilityValue.Hidden
            )
        )
        userLanguagesPreviewOptionItemsLiveData.value = items
    }

    fun updatePreviewTitle(title: String) {
        createPreviewRequest?.title = title
        setupPreviewItems()
    }

    fun updatePreviewLifetimeOption(item: PreviewLifetimeOption) {
        createPreviewRequest?.previewLifetimeOption = item
        setupPreviewItems()
    }

    fun updatePreviewVisibilityOption(item: PreviewVisibilityPattern) {
        when (item) {
            PreviewVisibilityPattern.Visible -> {
                createPreviewRequest?.apply {
                    userImageVisibility = PreviewVisibilityValue.Visible
                    userNameVisibility = PreviewVisibilityValue.Visible
                    userCurrentPositionVisibility = PreviewVisibilityValue.Visible
                    userLocationVisibility = PreviewVisibilityValue.Visible
                    userEmailVisibility = PreviewVisibilityValue.Visible
                    userUsernameVisibility = PreviewVisibilityValue.Visible
                    userBioVisibility = PreviewVisibilityValue.Visible
                    userSkillsVisibility = PreviewVisibilityValue.Visible
                    userExperienceVisibility = PreviewVisibilityValue.Visible
                    userEducationVisibility = PreviewVisibilityValue.Visible
                    userLanguagesVisibility = PreviewVisibilityValue.Visible
                }
            }
            PreviewVisibilityPattern.Anonymous -> {
                createPreviewRequest?.apply {
                    userImageVisibility = PreviewVisibilityValue.Hidden
                    userNameVisibility = PreviewVisibilityValue.Hidden
                    userCurrentPositionVisibility = PreviewVisibilityValue.PartiallyVisible
                    userLocationVisibility = PreviewVisibilityValue.PartiallyVisible
                    userEmailVisibility = PreviewVisibilityValue.Hidden
                    userUsernameVisibility = PreviewVisibilityValue.Hidden
                    userBioVisibility = PreviewVisibilityValue.Visible
                    userSkillsVisibility = PreviewVisibilityValue.Visible
                    userExperienceVisibility = PreviewVisibilityValue.PartiallyVisible
                    userEducationVisibility = PreviewVisibilityValue.PartiallyVisible
                    userLanguagesVisibility = PreviewVisibilityValue.Visible
                }
            }
        }
        setupPreviewItems()
    }

    fun updateUserImagePreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userImageVisibility = value
        setupPreviewItems()
    }

    fun updateUserNamePreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userNameVisibility = value
        setupPreviewItems()
    }

    fun updateUserPositionPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userCurrentPositionVisibility = value
        setupPreviewItems()
    }

    fun updateUserLocationPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userLocationVisibility = value
        setupPreviewItems()
    }

    fun updateUserEmailPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userEmailVisibility = value
        setupPreviewItems()
    }

    fun updateUserUsernamePreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userUsernameVisibility = value
        setupPreviewItems()
    }

    fun updateUserBioPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userBioVisibility = value
        setupPreviewItems()
    }

    fun updateUserSkillsPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userSkillsVisibility = value
        setupPreviewItems()
    }

    fun updateUserExperiencePreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userExperienceVisibility = value
        setupPreviewItems()
    }

    fun updateUserEducationPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userEducationVisibility = value
        setupPreviewItems()
    }

    fun updateUserLanguagesPreviewVisibilityValue(value: PreviewVisibilityValue) {
        createPreviewRequest?.userLanguagesVisibility = value
        setupPreviewItems()
    }

    fun deletePreview() {
        deletePreviewUseCase.launch(
            param = DeletePreviewUseCase.Params(id = previewId),
            block = {
                onComplete = { navigateBack() }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    private fun getPreview() {
        getPreviewUseCase.launch(
            param = GetPreviewUseCase.Params(id = previewId),
            block = {
                onComplete = { userPreview ->
                    when (userPreview) {
                        null -> {
                            emptyStateLiveData.value = EmptyState.Removed
                        }
                        else -> {
                            user = userPreview.user
                            createPreviewRequest = CreatePreviewRequest(
                                title = userPreview.preview.title,
                                previewLifetimeOption = userPreview.preview.previewLifetimeOption,
                                userImageVisibility = userPreview.preview.userImageVisibility,
                                userNameVisibility = userPreview.preview.userNameVisibility,
                                userCurrentPositionVisibility = userPreview.preview.userCurrentPositionVisibility,
                                userLocationVisibility = userPreview.preview.userLocationVisibility,
                                userEmailVisibility = userPreview.preview.userEmailVisibility,
                                userUsernameVisibility = userPreview.preview.userUsernameVisibility,
                                userBioVisibility = userPreview.preview.userBioVisibility,
                                userSkillsVisibility = userPreview.preview.userSkillsVisibility,
                                userExperienceVisibility = userPreview.preview.userExperienceVisibility,
                                userEducationVisibility = userPreview.preview.userEducationVisibility,
                                userLanguagesVisibility = userPreview.preview.userLanguagesVisibility
                            )
                        }
                    }
                    previousCreatePreviewRequest = createPreviewRequest?.copy()
                    setupPreviewItems()
                    isDeleteButtonVisibleLiveData.value = isEditMode() && userPreview != null
                }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    private fun setupPreviewItems() {
        val user = this.user ?: return
        val createPreviewRequest = this.createPreviewRequest ?: return
        val tempUserPreview = UserTempPreview(
            user = user,
            createPreviewRequest = createPreviewRequest
        )
        isAddButtonEnabledLiveData.value = isAddButtonEnabled()
        previewHeaderLiveData.value = PreviewHeader(
            titleString = createPreviewRequest.title,
            previewLifetimeOption = createPreviewRequest.previewLifetimeOption,
            previewVisibilityPattern = getPreviewVisibilityPattern()
        )
        viewModelScope.launch(dispatcherProvider.main) {
            previewUiLiveData.value = fromUserTempPreviewToPreviewUiOnAddMapper.map(tempUserPreview)
        }
    }

    private fun isAddButtonEnabled(): Boolean {
        return !isEditMode() || createPreviewRequest != previousCreatePreviewRequest
    }

    private fun getPreviewVisibilityPattern(): PreviewVisibilityPattern? {
        return createPreviewRequest?.run {
            val visiblePreviewVisibilityPatternCondition = mutableListOf<Boolean>().apply {
                if (user?.userImage != null) add(userImageVisibility == PreviewVisibilityValue.Visible)
                if (user?.userName != null) add(userNameVisibility == PreviewVisibilityValue.Visible)
                if (user?.getCurrentUserExperience() != null) add(userCurrentPositionVisibility == PreviewVisibilityValue.Visible)
                if (user?.userLocation != null) add(userLocationVisibility == PreviewVisibilityValue.Visible)
                add(userEmailVisibility == PreviewVisibilityValue.Visible)
                if (user?.userUsername.isNullOrEmpty().not()) add(userUsernameVisibility == PreviewVisibilityValue.Visible)
                if (user?.userBio.isNullOrEmpty().not()) add(userBioVisibility == PreviewVisibilityValue.Visible)
                if (user?.userSkills.isNullOrEmpty().not()) add(userSkillsVisibility == PreviewVisibilityValue.Visible)
                if (user?.userExperience.isNullOrEmpty().not()) add(userExperienceVisibility == PreviewVisibilityValue.Visible)
                if (user?.userEducation.isNullOrEmpty().not()) add(userEducationVisibility == PreviewVisibilityValue.Visible)
                if (user?.userLanguages.isNullOrEmpty().not()) add(userLanguagesVisibility == PreviewVisibilityValue.Visible)
            }.all { it }
            val anonymousPreviewVisibilityPatternCondition = mutableListOf<Boolean>().apply {
                if (user?.userImage != null) add(userImageVisibility == PreviewVisibilityValue.Hidden)
                if (user?.userName != null) add(userNameVisibility == PreviewVisibilityValue.Hidden)
                if (user?.getCurrentUserExperience() != null) add(userCurrentPositionVisibility == PreviewVisibilityValue.PartiallyVisible)
                if (user?.userLocation != null) add(userLocationVisibility == PreviewVisibilityValue.PartiallyVisible)
                add(userEmailVisibility == PreviewVisibilityValue.Hidden)
                if (user?.userUsername.isNullOrEmpty().not()) add(userUsernameVisibility == PreviewVisibilityValue.Hidden)
                if (user?.userBio.isNullOrEmpty().not()) add(userBioVisibility == PreviewVisibilityValue.Visible)
                if (user?.userSkills.isNullOrEmpty().not()) add(userSkillsVisibility == PreviewVisibilityValue.Visible)
                if (user?.userExperience.isNullOrEmpty().not()) add(userExperienceVisibility == PreviewVisibilityValue.PartiallyVisible)
                if (user?.userEducation.isNullOrEmpty().not()) add(userEducationVisibility == PreviewVisibilityValue.PartiallyVisible)
                if (user?.userLanguages.isNullOrEmpty().not()) add(userLanguagesVisibility == PreviewVisibilityValue.Visible)
            }.all { it }
            when {
                visiblePreviewVisibilityPatternCondition -> PreviewVisibilityPattern.Visible
                anonymousPreviewVisibilityPatternCondition -> PreviewVisibilityPattern.Anonymous
                else -> null
            }
        }
    }

    private fun clearErrors() {
        previewTitleErrorMessageLiveData.value = null
    }

    private fun isEditMode(): Boolean = previewId.isNotEmpty()

    private fun handleCreationError(throwable: Throwable) {
        handleError(throwable)
        (throwable as? InputValidationException)
            ?.let { inputValidationException ->
                inputValidationException.errors.forEach { error ->
                    when (error.message) {
                        is ValidationResultErrorMessage.PreviewTitle -> {
                            previewTitleErrorMessageLiveData.value =
                                error.message.message
                        }
                        else -> Unit
                    }
                }
            }
            ?: showError(throwable.message)
    }

    sealed class Mode {
        object Add : Mode()
        object Edit : Mode()
    }

    sealed class EmptyState {
        object Removed : EmptyState()
    }

    data class PreviewHeader(
        val titleString: String,
        val previewLifetimeOption: PreviewLifetimeOption,
        val previewVisibilityPattern: PreviewVisibilityPattern?
    )
}
