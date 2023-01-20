package com.wurple.domain.usecase.auth

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.AuthGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class RequestDeleteAccountUseCase(
    private val authGateway: AuthGateway,
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, RequestDeleteAccountUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        val email = userGateway.getCurrentUser().userEmail
        authGateway.requestDeleteAccount(email)
    }

    class Params
}