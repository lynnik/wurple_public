package com.wurple.domain.usecase.location

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.location.LocationManager
import com.wurple.domain.model.location.Location
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class GetLocationByIdUseCase(
    private val locationManager: LocationManager,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Location, GetLocationByIdUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): Location {
        return locationManager.getLocationById(params.id)
    }

    class Params(val id: String)
}