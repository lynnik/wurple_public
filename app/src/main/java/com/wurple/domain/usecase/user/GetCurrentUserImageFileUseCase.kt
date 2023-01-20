package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import java.io.File
import kotlin.coroutines.CoroutineContext

class GetCurrentUserImageFileUseCase(
    private val userGateway: UserGateway,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<File, GetCurrentUserImageFileUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params): File = userGateway.getUserImageFile()

    class Params
}