package com.wurple.domain.usecase.info

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.InfoGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import kotlin.coroutines.CoroutineContext

class SetFillOutProfileAsShownUseCase(
    private val infoGateway: InfoGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, SetFillOutProfileAsShownUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        infoGateway.setFillOutProfileAsShown()
    }

    class Params
}
