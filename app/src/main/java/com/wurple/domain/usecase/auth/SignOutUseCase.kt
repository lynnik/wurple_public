package com.wurple.domain.usecase.auth

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.AuthGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class SignOutUseCase(
    private val clearCacheUseCase: ClearCacheUseCase,
    private val authGateway: AuthGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, SignOutUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        clearCacheUseCase.executeOnBackground(ClearCacheUseCase.Params())
        authGateway.signOut()
    }

    class Params
}