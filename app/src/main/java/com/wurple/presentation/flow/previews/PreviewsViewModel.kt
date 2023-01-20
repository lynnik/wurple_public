package com.wurple.presentation.flow.previews

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.date.DateManager
import com.wurple.domain.log.Logger
import com.wurple.domain.model.preview.UserPreview
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.deeplink.GetPreviewDeepLinkUseCase
import com.wurple.domain.usecase.preview.FetchUserPreviewUseCase
import com.wurple.domain.usecase.preview.ObserveUserPreviewUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import com.wurple.presentation.model.preview.PreviewListItem

class PreviewsViewModel(
    private val dateManager: DateManager,
    private val observeUserPreviewUseCase: ObserveUserPreviewUseCase,
    private val fetchUserPreviewUseCase: FetchUserPreviewUseCase,
    private val getPreviewDeepLinkUseCase: GetPreviewDeepLinkUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val previewsLiveData = MutableLiveData<List<PreviewListItem>>()

    // previewTitle to deepLink
    val previewDeepLinkLiveData = SingleLiveEvent<Pair<String, String>>()
    val showSkeletonLiveData = SingleLiveEvent<Unit>()
    val hideSkeletonLiveData = SingleLiveEvent<Unit>()
    private var isSkeletonHid = false

    init {
        showSkeletonLiveData.call()
        observePreview()
        fetchPreview()
    }

    fun navigateEditPreview(previewId: String) {
        navigateForwardTo(Screen.PreviewsAdd(previewId))
    }

    fun navigatePreviewsAdd() {
        navigateForwardTo(Screen.PreviewsAdd())
    }

    fun navigatePreview(previewId: String) {
        navigateForwardTo(Screen.Preview(previewId))
    }

    fun fetchPreview(shouldShowProgress: Boolean = true) {
        fetchUserPreviewUseCase.launch(
            param = FetchUserPreviewUseCase.Params(),
            block = {
                if (shouldShowProgress) {
                    onStart = { showProgress() }
                }
                onTerminate = { hideProgress() }
                onError = ::handleError
            }
        )
    }

    fun showPreviewDeeplink(previewId: String, previewTitle: String) {
        getPreviewDeepLinkUseCase.launch(
            param = GetPreviewDeepLinkUseCase.Params(previewId),
            block = {
                onComplete = { deepLink ->
                    deepLink?.let { previewDeepLinkLiveData.value = previewTitle to it }
                }
                onStart = { showProgress() }
                onTerminate = { hideProgress() }
                onError = ::handleError
            }
        )
    }

    private fun observePreview() {
        observeUserPreviewUseCase.launch(
            param = ObserveUserPreviewUseCase.Params(),
            block = {
                onNext = { userPreviews ->
                    previewsLiveData.value = userPreviews
                        .sortedWith(
                            compareBy<UserPreview> { it.preview.isExpired }
                                .thenByDescending { it.preview.properties.updatedAt.baseDate.toString() }
                                .thenBy { it.preview.title }
                        )
                        .map { userPreview -> mapToPreviewListItem(userPreview) }

                    if (isSkeletonHid.not()) {
                        isSkeletonHid = true
                        hideSkeletonLiveData.call()
                    }
                }
            }
        )
    }

    private fun mapToPreviewListItem(userPreview: UserPreview): PreviewListItem {
        return PreviewListItem(
            previewId = userPreview.preview.id,
            title = userPreview.preview.title,
            formattedExpDate = userPreview.preview.expDate.formattedDate,
            differenceBetweenCurrentDateAndExpDate = dateManager.getDifferenceFromCurrentDateToDateInDays(
                userPreview.preview.expDate.baseDate
            ),
            isExpired = userPreview.preview.isExpired,
            userImageVisibility = userPreview.preview.userImageVisibility,
            userNameVisibility = userPreview.preview.userNameVisibility,
            userCurrentPositionVisibility = if (userPreview.user.getCurrentUserExperience() == null) {
                null
            } else {
                userPreview.preview.userCurrentPositionVisibility
            },
            userLocationVisibility = if (userPreview.user.userLocation == null) {
                null
            } else {
                userPreview.preview.userLocationVisibility
            },
            userEmailVisibility = userPreview.preview.userEmailVisibility,
            userUsernameVisibility = if (userPreview.user.userUsername.isEmpty()) {
                null
            } else {
                userPreview.preview.userUsernameVisibility
            },
            userBioVisibility = if (userPreview.user.userBio.isEmpty()) {
                null
            } else {
                userPreview.preview.userBioVisibility
            },
            userSkillsVisibility = if (userPreview.user.userSkills.isEmpty()) {
                null
            } else {
                userPreview.preview.userSkillsVisibility
            },
            userExperienceVisibility = if (userPreview.user.userExperience.isEmpty()) {
                null
            } else {
                userPreview.preview.userExperienceVisibility
            },
            userEducationVisibility = if (userPreview.user.userEducation.isEmpty()) {
                null
            } else {
                userPreview.preview.userEducationVisibility
            },
            userLanguagesVisibility = if (userPreview.user.userLanguages.isEmpty()) {
                null
            } else {
                userPreview.preview.userLanguagesVisibility
            }
        )
    }
}