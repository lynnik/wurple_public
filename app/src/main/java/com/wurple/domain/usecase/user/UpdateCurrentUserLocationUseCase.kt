package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.location.LocationManager
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class UpdateCurrentUserLocationUseCase(
    private val userGateway: UserGateway,
    private val locationManager: LocationManager,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, UpdateCurrentUserLocationUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        val location = locationManager.getLocationById(params.id)
        val userUpdateRequest = UserUpdateRequest(locationId = location.id)
        userGateway.updateCurrentUser(userUpdateRequest)
    }

    class Params(val id: String)
}