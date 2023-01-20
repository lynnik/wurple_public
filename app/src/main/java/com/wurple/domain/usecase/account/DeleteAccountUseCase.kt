package com.wurple.domain.usecase.account

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.AccountGateway
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.auth.ClearCacheUseCase
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class DeleteAccountUseCase(
    private val clearCacheUseCase: ClearCacheUseCase,
    private val userGateway: UserGateway,
    private val accountGateway: AccountGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, DeleteAccountUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        clearCacheUseCase.executeOnBackground(ClearCacheUseCase.Params())
        val currentUserId = userGateway.getCurrentUserId()
        accountGateway.deleteAccount(currentUserId)
    }

    class Params
}