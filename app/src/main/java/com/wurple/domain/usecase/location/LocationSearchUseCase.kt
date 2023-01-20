package com.wurple.domain.usecase.location

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.location.LocationManager
import com.wurple.domain.model.location.LocationSearch
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class LocationSearchUseCase(
    private val locationManager: LocationManager,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<List<LocationSearch>, LocationSearchUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): List<LocationSearch> {
        return locationManager.searchForLocation(params.query)
    }

    class Params(val query: String)
}