package com.wurple.presentation.flow.profile.edit.location

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.log.Logger
import com.wurple.domain.model.location.Location
import com.wurple.domain.model.location.LocationSearch
import com.wurple.domain.model.user.User
import com.wurple.domain.usecase.location.LocationSearchUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserLocationUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileLocationViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val locationSearchUseCase: LocationSearchUseCase,
    private val updateCurrentUserLocationUseCase: UpdateCurrentUserLocationUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val locationsLiveData = MutableLiveData<List<LocationSearch>>()
    val currentUserLocationLiveData = SingleLiveEvent<Location>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun searchForCurrentUserLocation(query: String) {
        locationSearchUseCase.launch(
            param = LocationSearchUseCase.Params(query),
            block = {
                onComplete = { locationSearchList ->
                    locationsLiveData.value = locationSearchList
                }
                onError = {
                    handleError(it)
                    showError(it.message)
                }
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    fun updateLocation(id: String) {
        updateCurrentUserLocationUseCase.launch(
            param = UpdateCurrentUserLocationUseCase.Params(id),
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

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileLocationViewModel.user = user
                    user.userLocation?.let { currentUserLocationLiveData.value = it }
                }
            }
        )
    }
}