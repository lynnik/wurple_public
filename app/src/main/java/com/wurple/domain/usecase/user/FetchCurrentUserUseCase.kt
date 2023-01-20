package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class FetchCurrentUserUseCase(
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, FetchCurrentUserUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        userGateway.fetchCurrentUser()
    }

    class Params
}