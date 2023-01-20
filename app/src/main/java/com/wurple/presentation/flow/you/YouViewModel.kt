package com.wurple.presentation.flow.you

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.info.GetFillOutProfileUseCase
import com.wurple.domain.usecase.info.SetFillOutProfileAsShownUseCase
import com.wurple.domain.usecase.onboarding.GetOnboardingUseCase
import com.wurple.domain.usecase.user.FetchCurrentUserUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import com.wurple.presentation.model.mapper.FromUserToPreviewUiMapper
import com.wurple.presentation.model.preview.PreviewUi
import kotlinx.coroutines.launch

class YouViewModel(
    private val getOnboardingUseCase: GetOnboardingUseCase,
    private val getFillOutProfileUseCase: GetFillOutProfileUseCase,
    private val setFillOutProfileAsShownUseCase: SetFillOutProfileAsShownUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val fromUserToPreviewUiMapper: FromUserToPreviewUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    logger: Logger
) : BaseViewModel(logger) {
    val isFillOutProfileViewVisibleLiveData = MutableLiveData(false)
    val previewUiLiveData = MutableLiveData<PreviewUi>()
    val showSkeletonLiveData = SingleLiveEvent<Unit>()
    val hideSkeletonLiveData = SingleLiveEvent<Unit>()
    private var isSkeletonHid = false

    init {
        showSkeletonLiveData.call()
        observeUser()
        fetchUser()
        shouldShowOnboarding()
        shouldShowFillOutProfile()
    }

    fun fetchUser(shouldShowProgress: Boolean = true) {
        fetchCurrentUserUseCase.launch(
            param = FetchCurrentUserUseCase.Params(),
            block = {
                if (shouldShowProgress) {
                    onStart = { showProgress() }
                }
                onTerminate = { hideProgress() }
                onError = ::handleError
            }
        )
    }

    fun navigateEditProfile() {
        navigateForwardTo(Screen.EditProfile)
    }

    fun navigateFillOutProfile() {
        setFillOutProfileAsShownUseCase.launch(
            param = SetFillOutProfileAsShownUseCase.Params(),
            block = {
                onComplete = {
                    isFillOutProfileViewVisibleLiveData.value = false
                    navigateEditProfile()
                }
                onError = ::handleError
            }
        )
    }

    fun hideFillOutProfileView() {
        setFillOutProfileAsShownUseCase.launch(
            param = SetFillOutProfileAsShownUseCase.Params(),
            block = {
                onComplete = {
                    isFillOutProfileViewVisibleLiveData.value = false
                }
                onError = ::handleError
            }
        )
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    viewModelScope.launch(dispatcherProvider.main) {
                        previewUiLiveData.value = fromUserToPreviewUiMapper.map(user)
                    }
                    if (isSkeletonHid.not()) {
                        isSkeletonHid = true
                        hideSkeletonLiveData.call()
                    }
                }
            }
        )
    }

    private fun shouldShowOnboarding() {
        getOnboardingUseCase.launch(
            param = GetOnboardingUseCase.Params(),
            block = {
                onComplete = { shouldShowOnboarding ->
                    if (shouldShowOnboarding) {
                        navigateForwardTo(Screen.Onboarding)
                    }
                }
                onError = ::handleError
            }
        )
    }

    private fun shouldShowFillOutProfile() {
        getFillOutProfileUseCase.launch(
            param = GetFillOutProfileUseCase.Params(),
            block = {
                onComplete = { shouldShowFillOutProfile ->
                    isFillOutProfileViewVisibleLiveData.value = shouldShowFillOutProfile
                }
                onError = ::handleError
            }
        )
    }
}
