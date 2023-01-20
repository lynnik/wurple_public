package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.model.user.User
import com.wurple.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class ObserveCurrentUserUseCase(
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : FlowUseCase<User, ObserveCurrentUserUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): Flow<User> =
        userGateway.observeCurrentUser()

    class Params
}