package com.wurple.domain.usecase.user

import com.wurple.domain.error.handler.ErrorHandler
import com.wurple.domain.gateway.UserGateway
import com.wurple.domain.usecase.base.CoroutineUseCase
import com.wurple.domain.validation.InputValidationRule
import com.wurple.domain.validation.InputValidator
import kotlin.coroutines.CoroutineContext

class UpdateCurrentUserEmailUseCase(
    private val userGateway: UserGateway,
    private val emailInputValidationRule: InputValidationRule<String>,
    private val inputValidator: InputValidator,
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler
) : CoroutineUseCase<Unit, UpdateCurrentUserEmailUseCase.Params>(
    coroutineContext,
    errorHandler
) {
    override suspend fun executeOnBackground(params: Params) {
        inputValidator.validate(emailInputValidationRule.validate(params.email))
        userGateway.updateCurrentUserEmail(params.email)
    }

    class Params(val email: String)
}